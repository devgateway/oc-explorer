/**
 * init function that is called from the Wicket Component
 */
const init = function (parameters) {
    'use strict';

    const chart = new PlotlyChart(parameters);
    chart.render();
};


/**
 * Use this if you want to configure some default properties for a plot.ly chart.
 */
PlotlyChart.prototype.defaultProps = {
    chartId:          'chartId',
    layout: {
        showlegend:    true,
    }
};

/**
 * Use this constructor in order to initialize a PlotlyChart.
 * If you want to render the chart use the `render()` function.
 */
function PlotlyChart(parameters) {
    this.props = Object.assign({}, this.defaultProps, parameters);
}

/**
 * Function that actually renders the chart using plot.ly
 */
PlotlyChart.prototype.render = function() {
    const { chartId, data, layout } = this.props;

    // console.log(JSON.stringify(this.props, null, '\t'));

    Plotly.newPlot(chartId, data, layout, {displayModeBar: true, displaylogo: false});
};
