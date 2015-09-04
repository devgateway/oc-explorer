import React from "react";
require('./example.less');

export default class App extends React.Component{
  render(){
    return (
      <div className="container">
        <div className="col-md-12">
          <div className="jumbotron">
            <h1>I'm a placeholder</h1>
            <p>Edit or delete me in ./components/app</p>
            <p>Documentation is in readme.md</p>
            <p><a className="btn btn-primary btn-lg" href="#" role="button">Awesome!</a></p>
          </div>
        </div>
      </div>
    )
  }
}