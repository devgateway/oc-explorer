import Plotly from 'plotly.js/lib/core';
import ReactIgnore from '../react-ignore';

class PlotlyChart extends React.PureComponent {
  componentDidMount() {
    const { data, layout } = this.props;
    Plotly.newPlot(
      this.chartContainer,
      data,
      layout,
    );
  }

  componentWillUnmount() {
    Plotly.Plots.purge(this.container);
  }

  render() {
    return (
      <div className="chart-container">
        <ReactIgnore>
          <div ref={(c) => { this.chartContainer = c; }} />
        </ReactIgnore>
      </div>
    );
  }
}

PlotlyChart.defaultProps = {
  data: [],
  layout: {},
}

export default PlotlyChart;
