import Component from "./pure-render-component";
import Plotly from "plotly.js/lib/core";
Plotly.register([
    require('plotly.js/lib/bar')
]);

export default class Plot extends Component{
  componentDidMount(){
    Plotly.newPlot(this.refs.chartContainer, this.getData(), this.getLayout());
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
    return (
        <section>
          <h4 className="page-header">{this.getTitle()}</h4>
          <div ref="chartContainer"></div>
        </section>
    )
  }
}