import { cloneChild } from '../tools';
import style from './style.less';

class Zoomable extends React.PureComponent {
  render() {
    return (
      <div className="zoomable">
        {cloneChild(this)}
        <button className="btn btn-default btn-sm zoom-button">
          <i className="glyphicon glyphicon-fullscreen"/>
        </button>
      </div>
    )
  }
}

export default Zoomable;
