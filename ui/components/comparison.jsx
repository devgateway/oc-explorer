import Component from "./pure-render-component";

export default class Comparison extends Component{
  render(){
    var {data, width, Component, title} = this.props;
    if(!data) return null;
    return (
        <section>
          <h4 className="page-header">{title}</h4>
          {data.map(datum => (
              <div className="col-xs-3">
                <Component data={datum} width={width} dontWrap={true}/>
              </div>
            )
          )}
        </section>
    )
  }
}