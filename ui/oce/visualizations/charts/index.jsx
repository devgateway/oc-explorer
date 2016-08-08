import Visualization from "../../visualization";
import ReactIgnore from "../../react-ignore";
import {max} from "../../tools";
import {Map} from "immutable";
import cn from "classnames";
import styles from "./index.less";
import Plotly from "plotly.js/lib/core";
Plotly.register([
  require('plotly.js/lib/bar')
]);

class Chart extends Visualization{
  getData(){
    return super.getData();
  }

  getDecoratedLayout(){
    var {title, xAxisRange, yAxisRange, styling, width, height, margin} = this.props;
    var layout = this.getLayout();
    layout.width = width;
    layout.height = height;
    layout.margin = margin;
    if(title) layout.title = title;
    if(xAxisRange) layout.xaxis.range = xAxisRange;
    if(yAxisRange) layout.yaxis.range = yAxisRange;
    if(styling){
      layout.xaxis.titlefont = {
        color: styling.charts.axisLabelColor
      };

      layout.yaxis.titlefont = {
        color: styling.charts.axisLabelColor
      }
    }
    return layout;
  }

  componentDidMount(){
    super.componentDidMount();
    Plotly.newPlot(this.refs.chartContainer, this.getData(), this.getDecoratedLayout());
  }

  componentWillUnmount(){
    Plotly.Plots.purge(this.refs.chartContainer);
  }

  componentDidUpdate(prevProps){
    super.componentDidUpdate(prevProps);
    if(this.constructor.UPDATABLE_FIELDS.some(prop => prevProps[prop] != this.props[prop]) || this.props.translations != prevProps.translations){
      this.refs.chartContainer.data = this.getData();
      this.refs.chartContainer.layout = this.getDecoratedLayout();
      setTimeout(() => Plotly.redraw(this.refs.chartContainer));
    } else if(['title', 'width', 'xAxisRange', 'yAxisRange'].some(prop => prevProps[prop] != this.props[prop])){
      setTimeout(() => Plotly.relayout(this.refs.chartContainer, this.getDecoratedLayout()));
    }
  }

  hasNoData(){
    return 0 == this.getData().length;
  }

  render(){
    let hasNoData = this.hasNoData();
    return <div className={cn("chart-container", {"no-data": hasNoData})}>
      {hasNoData && <div className="no-data-msg">{this.__('No data')}</div>}
      <ReactIgnore>
        <div ref="chartContainer"/>
      </ReactIgnore>
    </div>
  }
}

Chart.getFillerDatum = year => Map({year});

Chart.getMaxField = data => data.flatten().filter((value, key) => value && "year" != key).reduce(max, 0);

Chart.UPDATABLE_FIELDS = ['data'];

Chart.propTypes.styling = React.PropTypes.shape({
  charts: React.PropTypes.shape({
    axisLabelColor: React.PropTypes.string.isRequired,
    traceColors: React.PropTypes.arrayOf(React.PropTypes.string).isRequired
  }).isRequired
}).isRequired;

export default Chart;