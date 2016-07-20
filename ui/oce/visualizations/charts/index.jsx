import Visualization from "../../visualization";
import ReactIgnore from "../../react-ignore";
import {max} from "../../tools";
import {Map} from "immutable";
import Plotly from "plotly.js/lib/core";
Plotly.register([
  require('plotly.js/lib/bar')
]);

class Chart extends Visualization{
  getData(){
    return super.getData();
  }

  getDecoratedLayout(){
    var {title, xAxisRange, yAxisRange} = this.props;
    var layout = this.getLayout();
    layout.width = this.props.width;
    if(title) layout.title = title;
    if(xAxisRange) layout.xaxis.range = xAxisRange;
    if(yAxisRange) layout.yaxis.range = yAxisRange;
    return layout;
  }

  componentDidMount(){
    super.componentDidMount();
    Plotly.newPlot(this.refs.chartContainer, this.getData(), this.getDecoratedLayout());
  }

  componentDidUpdate(prevProps){
    super.componentDidUpdate(prevProps);
    if(this.constructor.UPDATABLE_FIELDS.some(prop => prevProps[prop] != this.props[prop])){
      this.refs.chartContainer.data = this.getData();
      Plotly.redraw(this.refs.chartContainer);
    }

    if(['title', 'width', 'xAxisRange', 'yAxisRange'].some(prop => prevProps[prop] != this.props[prop])){
      Plotly.relayout(this.refs.chartContainer, this.getDecoratedLayout());
    }
  }

  render(){
    return <ReactIgnore>
      <div ref="chartContainer"/>
    </ReactIgnore>
  }
}

Chart.getFillerDatum = year => Map({year});

Chart.getMaxField = data => data.flatten().filter((value, key) => value && "year" != key).reduce(max, 0);

Chart.UPDATABLE_FIELDS = ['data'];

export default Chart;