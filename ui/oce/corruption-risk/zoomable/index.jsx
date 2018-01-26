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
    const { zoomedWidth: width, children, ...props } = this.props;
    const { zoomed } = this.state;
    if (zoomed) {
      const style = {
        width,
        marginLeft: -(width / 2),
      };
      return (
        <div>
          <div className="crd-fullscreen-popup-overlay" onClick={e => this.setState({ zoomed: false })}/>
          <div className="crd-fullscreen-popup" style={ style }>
            {cloneChild(this, {
               ...props,
               width
            })}
          </div>
        </div>
      );
    }
  }

  interceptClicks({ target }) {
    if (target.className.indexOf('zoom-button') > -1) {
      this.setState({ zoomed: true });
    }
  }

  render() {
    const { zoomed } = this.state;
    const { cutData, data, children, ...props } = this.props;
    return (
      <div className="zoomable" onClick={this.interceptClicks.bind(this)}>
        {this.maybeGetZoomed()}
        {!zoomed && cloneChild(this, {
           ...props,
           data: cutData(data)
        })}
      </div>
    )
  }
}

export default Zoomable;
