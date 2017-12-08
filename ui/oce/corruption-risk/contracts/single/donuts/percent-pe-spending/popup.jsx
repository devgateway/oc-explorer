// eslint-disable-next-line no-unused-vars
const POPUP_WIDTH = 300;
const POPUP_HEIGHT = 90;

class PercentPEPopup extends React.Component {
  render() {
    const { x, y, points, data } = this.props;
    const total = data.get('total');
    const { v, label } = points[0];
    const left = x - (POPUP_WIDTH / 2) + 30;
    const top = y - POPUP_HEIGHT - 12;
    const value = v / total * 100;
    const formattedValue = Math.round(value) === value ?
      value :
      value.toFixed(2);

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
        {label.replace(/\$#\$/g, formattedValue)}
        <div className="arrow" />
      </div>
    );
  }
}

export default PercentPEPopup;
