// eslint-disable-next-line no-unused-vars
import style from './style.less';

const POPUP_WIDTH = 250;
const POPUP_HEIGHT = 75;

class DonutPopup extends React.Component {
  render() {
    const { x, y, points } = this.props;
    const { v: value, label } = points[0];
    const left = x - (POPUP_WIDTH / 2) + 30;
    const top = y - POPUP_HEIGHT - 12;
    return (
      <div
        className="crd-popup donut-popup"
        style={{
          width: POPUP_WIDTH,
          height: POPUP_HEIGHT,
          left,
          top,
          color: 'white'
        }}
      >
        {label}
        <div className="arrow" />
      </div>
    )
  }
}

export default DonutPopup;
