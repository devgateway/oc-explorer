import DataFetcher from '../../../data-fetcher';
import TaggedBarChart from '../../../tagged-bar-chart';
import { wireProps } from '../../../tools';
import { pluck, cacheFn } from '../../../../tools';
import translatable from '../../../../translatable';
import CustomPopup from '../../../custom-popup';

const POPUP_WIDTH = 350;
const POPUP_HEIGHT = 55;
const POPUP_ARROW_SIZE = 8;

class Popup extends React.PureComponent {
  render() {
    const { x, y, points } = this.props;
    const point = points[0];
    const { xaxis, yaxis } = point;
    const markerLeft = xaxis.l2p(point.x) + xaxis._offset;
    const markerTop = yaxis.l2p(point.pointNumber) + yaxis._offset;

    const left = (markerLeft / 2) - (POPUP_WIDTH / 2);
    const top = markerTop - POPUP_HEIGHT - (POPUP_ARROW_SIZE * 1.5);

    const style = {
      left,
      top,
      width: POPUP_WIDTH,
      height: POPUP_HEIGHT
    };

    const flags = point.x;

    return (
      <div
        className="crd-popup donut-popup text-center"
        style={style}
      >
        {flags} {flags === 1 ? 'flag' : 'flags'} for "{point.y}"
        <div className="arrow"/>
      </div>
    )
  }
}

class FlaggedNr extends translatable(React.PureComponent) {
  render() {
    const { width, data } = this.props;
    return (
      <TaggedBarChart
        data={data}
        width={width}
        tags={{
          FRAUD: {
            name: 'Fraud',
            color: '#299df4',
          },
          RIGGING: {
            name: 'Process rigging',
            color: '#3372b2',
          },
          COLLUSION: {
            name: 'Collusion',
            color: '#fbc42c',
          },
        }}
      />
    );
  }
}

class FlaggedNrWrapper extends translatable(React.PureComponent) {
  constructor(...args){
    super(...args);
    this.getEndpoints = cacheFn(indicatorTypesMapping =>
      Object.keys(indicatorTypesMapping).map(id => `flags/${id}/count`)
    );
  }

  onRequestNewData(path, data) {
    const { indicatorTypesMapping } = this.props;
    const chartData = [];
    if (Array.isArray(data)) {
      Object.keys(indicatorTypesMapping).forEach((id, index) => {
        if(data[index].length) {
          chartData.push({
            x: data[index][0].count,
            y: this.t(`crd:indicators:${id}:name`),
            tags: indicatorTypesMapping[id].types
          });
        }
      });
      chartData.sort((a, b) => b.x - a.x);
    }
    this.props.requestNewData(path, chartData);
  }

  render() {
    const { requestNewData, indicatorTypesMapping } = this.props;
    if (!requestNewData) return null;
    const endpoints = this.getEndpoints(indicatorTypesMapping);
    return (
      <DataFetcher
        {...this.props}
        requestNewData={this.onRequestNewData.bind(this)}
        endpoints={endpoints}
      >
        <CustomPopup
          {...this.props}
          Chart={FlaggedNr}
          Popup={Popup}
        />
      </DataFetcher>
    )
  }
}
export default FlaggedNrWrapper;
