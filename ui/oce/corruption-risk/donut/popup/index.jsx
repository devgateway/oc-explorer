// eslint-disable-next-line no-unused-vars
import style from './style.less';

const POPUP_WIDTH = 300;

class DonutPopup extends React.Component {
  render() {
    const { x, y, points } = this.props;
    const { v: value, label } = points[0];
    const formattedValue = Math.round(value) === value ?
      value :
      value.toFixed(2);

    const text = label.replace(/\$#\$/g, formattedValue);

    let POPUP_HEIGHT = 55;


    if (text.length >= 40) POPUP_HEIGHT = 75;
    if (text.length >= 65) POPUP_HEIGHT = 90;

    const left = x - (POPUP_WIDTH / 2) + 30;
    const top = y - POPUP_HEIGHT - 12;

    return (
      <div
        className="crd-popup donut-popup text-center"
        style={{
          width: POPUP_WIDTH,
          height: POPUP_HEIGHT,
          left,
          top,
          color: 'white',
        }}
      >
        {text}
        <div className="arrow" />
      </div>
    );
  }
}

export default DonutPopup;
