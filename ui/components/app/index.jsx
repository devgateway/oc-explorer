import React from "react";
import Counter from "./counter";
require('./style.less');

export default class App extends React.Component{
  render(){
    var {state, actions} = this.props;
    return (
      <div className="container">
        <div className="col-md-12">
          <div className="jumbotron">
            <h1>I'm just an example</h1>
            <p>Edit or delete me in ./components/app</p>
            <p>Documentation is in readme.md</p>
            <Counter value={state.getIn(['globalState', 'counter'])} actions={actions}/>
          </div>
        </div>
      </div>
    )
  }
}