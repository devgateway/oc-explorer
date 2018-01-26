import DataFetcher from '../../../data-fetcher';
import TaggedBarChart from '../../../tagged-bar-chart';
import { wireProps } from '../../../tools';
import { pluck, cacheFn } from '../../../../tools';
import translatable from '../../../../translatable';

class FlaggedNr extends translatable(React.PureComponent) {
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
            x: this.t(`crd:indicators:${id}:name`),
            y: data[index][0].count,
            tags: indicatorTypesMapping[id].types
          });
        }
      });
      chartData.sort((a, b) => b.y - a.y);
    }
    this.props.requestNewData(path, chartData);
  }

  render() {
    const { requestNewData, indicatorTypesMapping, width } = this.props;
    if (!requestNewData) return null;
    const endpoints = this.getEndpoints(indicatorTypesMapping);
    return (
      <DataFetcher
        {...wireProps(this)}
        requestNewData={this.onRequestNewData.bind(this)}
        endpoints={endpoints}
      >
        <TaggedBarChart
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
      </DataFetcher>
    );
  }
}

export default FlaggedNr;
