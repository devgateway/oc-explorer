import {findDOMNode} from "react-dom";
import cn from "classnames";

class ThreeLineText extends React.PureComponent{
  constructor(...args){
    super(...args);
    this.state = {};
  }

  checkOverflow(){
    const el = findDOMNode(this);
    console.log(el.clientHeight, el.scrollHeight);
    this.setState({
      isOverflowing: el.clientHeight < el.scrollHeight
    });
  }

  componentDidMount(){
    this.checkOverflow();
  }

  componentDidUpdate(){
    this.checkOverflow();
  }

  render(){
    const {text} = this.props;
    const {isOverflowing} = this.state;
    return (
      <div className={cn("oce-3-line-text", {overflown: isOverflowing})}>
        {text}
      </div>
    )
  }
}
