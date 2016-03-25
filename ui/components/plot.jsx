import Component from "./pure-render-component";
import Plotly from "plotly.js/lib/core";
Plotly.register([
    require('plotly.js/lib/bar')
]);

class Plot extends Component{
  componentDidMount(){
    var {pageHeaderTitle, title, xAxisRange, yAxisRange} = this.props;
    var layout = this.getLayout();
    if(!pageHeaderTitle) layout.title = title;
    if(xAxisRange) layout.xaxis.range = xAxisRange;
    if(yAxisRange) layout.yaxis.range = yAxisRange;
    Plotly.newPlot(this.refs.chartContainer, this.getData(), layout);
  }

  componentDidUpdate(prevProps){
    if(prevProps.width != this.props.width){
      Plotly.Plots.resize(this.refs.chartContainer);
    } else {
      //TODO: more efficient and non-redundant updates
      this.refs.chartContainer.data = this.getData();
      Plotly.redraw(this.refs.chartContainer);
    }
  }

  render(){
    var {pageHeaderTitle, title} = this.props;
    return (
        <section>
          {pageHeaderTitle && <h4 className="page-header">{title}</h4>}
          <div ref="chartContainer"></div>
        </section>
    )
  }
}

Plot.defaultProps = {
  pageHeaderTitle: true
}

export default Plot;
