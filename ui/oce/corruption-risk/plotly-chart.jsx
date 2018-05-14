import Plotly from 'plotly.js/lib/core';
import ReactIgnore from '../react-ignore';

class PlotlyChart extends React.PureComponent {
  componentDidMount() {
    const { data, layout, onUpdate } = this.props;
    Plotly.newPlot(
      this.chartContainer,
      data,
      layout,
    );
    if (onUpdate) this.chartContainer.on('plotly_afterplot', onUpdate);
  }

  componentWillUnmount() {
    Plotly.Plots.purge(this.chartContainer);
  }

  componentDidUpdate(prevProps) {
    const { data, layout } = this.props;
    if (data !== prevProps[data]) {
      this.chartContainer.data = data;
      this.chartContainer.layout = layout;
      setTimeout(() => Plotly.redraw(this.chartContainer));
    } else if (layout !== prevProps[layout]) {
      setTimeout(() => Plotly.relayout(this.chartContainer, layout));
    }
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
