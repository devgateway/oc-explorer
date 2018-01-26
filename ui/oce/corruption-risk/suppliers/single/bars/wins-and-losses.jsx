import PlotlyChart from '../../../plotly-chart';
import DataFetcher from '../../../data-fetcher';
import { wireProps } from '../../../tools';
import { pluck } from '../../../../tools';

class WinsBarChart extends React.PureComponent {
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
    }, {
      x: names,
      y: data.map(pluck('countFlags')),
      name: 'Flags',
      type: 'bar',
      marker: {
        color: '#ce4747',
      },
    }]);
  }

  render() {
    const { width, requestNewData } = this.props;
    if (!requestNewData) return null;
    return (
      <DataFetcher
        {...wireProps(this)}
        endpoint="supplierWinsPerProcuringEntity"
        requestNewData={this.onRequestNewData.bind(this)}
      >
        <PlotlyChart
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
      </DataFetcher>
    );
  }
}

export default WinsBarChart;
