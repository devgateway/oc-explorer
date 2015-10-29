import React, {PropTypes} from "react";
import Component from "../../pure-render-component";
require('./style.less');

export default class Counter extends Component{
  render(){
    var {value, actions} = this.props;
    return (
      <p className="example-counter">
        <i className="glyphicon glyphicon-minus" onClick={e => actions.decCounter()}/>&nbsp;
        {value}&nbsp;
        <i className="glyphicon glyphicon-plus" onClick={e => actions.incCounter()}/>
      </p>
    )
  }
}

Counter.propTypes = {
  actions: PropTypes.shape({
    incCounter: PropTypes.func.isRequired,
    decCounter: PropTypes.func.isRequired
  }).isRequired
};