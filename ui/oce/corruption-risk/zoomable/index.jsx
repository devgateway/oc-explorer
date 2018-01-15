import cn from 'classnames';
import { cloneChild } from '../tools';
import style from './style.less';

class Zoomable extends React.PureComponent {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.zoomed = false;
  }

  maybeGetZoomed() {
    const { zoomedWidth: width } = this.props;
    const { zoomed } = this.state;
    if (zoomed) {
      const style = {
        width,
        marginLeft: -(width / 2),
      }
      return (
        <div>
          <div className="crd-fullscreen-popup-overlay" onClick={e => this.setState({ zoomed: false })}/>
          <div className="crd-fullscreen-popup" style={ style }>
            {cloneChild(this, { width })}
          </div>
        </div>
      );
    }
  }

  render() {
    const { zoomed } = this.state;
    const { width } = this.props;
    return (
      <div className="zoomable">
        {this.maybeGetZoomed()}
        {!zoomed && cloneChild(this, { width })}
        <button className="btn btn-default btn-sm zoom-button" onClick={e => this.setState({ zoomed: true })}>
          <i className="glyphicon glyphicon-fullscreen"/>
        </button>
      </div>
    )
  }
}

export default Zoomable;
