import PlotlyChart from '../../../plotly-chart';
import DataFetcher from '../../../data-fetcher';
import { wireProps } from '../../../tools';
import { pluck } from '../../../../tools';
import CustomPopup from '../../../custom-popup';

const POPUP_WIDTH = 300;
const POPUP_HEIGHT = 75;
const POPUP_ARROW_SIZE = 8;

class Popup extends React.PureComponent {
  render() {
    const { x, y, points } = this.props;
    const [a, b] = points;
    const point = a.y > b.y ? a : b;
    const { xaxis, yaxis } = point;
    const markerLeft = xaxis.l2p(point.pointNumber) + xaxis._offset;
    const markerTop = yaxis.l2p(point.y) + yaxis._offset;

    const left = markerLeft - (POPUP_WIDTH / 2);
    const top = markerTop - POPUP_HEIGHT - (POPUP_ARROW_SIZE * 1.5);

    const style = {
      left,
      top,
      width: POPUP_WIDTH,
      height: POPUP_HEIGHT
    };

    const wins = points[0].y

    return (
      <div
        className="crd-popup donut-popup text-center"
        style={style}
      >
        This supplier has {wins} {wins === 1 ? 'win' : 'wins'} from supplier {points[0].x} with {points[1].y} flags
        <div className="arrow"/>
      </div>
    )
  }
}

class WinsBarChart extends React.PureComponent {
  render() {
    const { width, data } = this.props;
    return (
      <PlotlyChart
        data={data}
        layout={{
          width,
          height: 250,
          barmode: 'group',
          margin: {t: 0, r: 20, b: 30, l: 20, pad: 0},
          paper_bgcolor: 'rgba(0, 0, 0, 0)',
          plot_bgcolor: 'rgba(0, 0, 0, 0)',
          legend: {
            xanchor: 'right',
            yanchor: 'top',
            x: .9,
            y: 1.5,
            orientation: 'h',
          }
        }}
      />
    );
  }
}

class WinsBarChartWrapper extends React.PureComponent {
  onRequestNewData(path, data) {
    const names = data.map(pluck('procuringEntityName'));
    this.props.requestNewData(path, [{
      x: names,
      y: data.map(pluck('count')),
      name: 'Wins',
      type: 'bar',
      marker: {
        color: '#289df4',
      },
      hoverinfo: 'none'
    }, {
      x: names,
      y: data.map(pluck('countFlags')),
      name: 'Flags',
      type: 'bar',
      marker: {
        color: '#ce4747',
      },
      hoverinfo: 'none'
    }]);
  }

  render() {
    const { requestNewData } = this.props;
    if (!requestNewData) return null;

    return (
      <DataFetcher
        {...this.props}
        endpoint="supplierWinsPerProcuringEntity"
        requestNewData={this.onRequestNewData.bind(this)}
      >
        <CustomPopup
          {...this.props}
          Chart={WinsBarChart}
          Popup={Popup}
        />
      </DataFetcher>
    );
  }
}

export default WinsBarChartWrapper;
