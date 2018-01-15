import PlotlyChart from '../../../plotly-chart';

class WinsBarChart extends React.PureComponent {
  render() {
    const { width } = this.props;
    return (
      <PlotlyChart
        data={[{
            x: ['Supplier 1', 'Supplier 2', 'Supplier 3', 'Supplier 4', 'Supplier 5'],
            y: [1, 2, 3, 4, 5],
            name: 'Wins',
            type: 'bar',
        }, {
            x: ['Supplier 1', 'Supplier 2', 'Supplier 3', 'Supplier 4', 'Supplier 5'],
            y: [5, 4, 3, 2, 1],
            name: 'Flags',
            type: 'bar',
        }]}
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

export default WinsBarChart;
