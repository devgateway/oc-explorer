import Component from "./pure-render-component";

export default class Comparison extends Component{
  render(){
    var {state, Component, title, width} = this.props;
    var {data, yAxisRange, xAxisRange} = state;
    return (
        <div>
          <h3 className="page-header">{title}</h3>
          {data.map((datum, index) => (
             <Component
                 key={index}
                 yAxisRange={yAxisRange}
                 xAxisRange={xAxisRange}
                 data={datum}
                 pageHeaderTitle={false}
                 title={index}
                 width={width}
             />
          ))}
        </div>
    )
  }
}