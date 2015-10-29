var React = require('react');
function shallowDiff (a,b){
  if(a && b && "object" == typeof a && "object" == typeof b){
    return Object.keys(a).some(key => a[key] != b[key]);
  } else {
    return a != b;
  }
}

export default class PureRenderComponent extends React.Component {
  shouldComponentUpdate (nextProps, nextState){
    return shallowDiff(this.props, nextProps) || shallowDiff(this.state, nextState);
  }
};