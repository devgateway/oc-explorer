import Component from "./pure-render-component";

export default class Comparison extends Component{
  render(){
    var {data, Component, title} = this.props;
    if(!data) return null;
    return (
        <div>
          <h3 className="page-header">{title}</h3>
          {data.map((datum, index) => (
             <Component {...this.props} key={index} data={datum} pageHeaderTitle={false} title={index}/>
          ))}
        </div>
    )
  }
}