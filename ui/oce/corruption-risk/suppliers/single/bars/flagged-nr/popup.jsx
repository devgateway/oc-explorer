import translatable from '../../../../../translatable';

class Popup extends translatable(React.PureComponent) {
  render() {
    const { coordinate, active, viewBox, payload } = this.props;
    if (!active || !payload[0]) return null;

    const { count, indicatorId } = payload[0].payload;

    let POPUP_HEIGHT = 55;

    const style = {
      left: 0,
      top: coordinate.y - POPUP_HEIGHT - viewBox.top - 4,
      width: 350,
      height: POPUP_HEIGHT,
    }

    const label = count === 1 ?
      this.t('crd:supplier:flaggedNr:popup:sg') :
      this.t('crd:supplier:flaggedNr:popup:pl');

    const indicatorName = this.t(`crd:indicators:${indicatorId}:name`);

    return (
      <div>
        <div
          className="crd-popup donut-popup text-center"
          style={style}
        >
          {label.replace('$#$', count).replace('$#$', indicatorName)}
          <div className="arrow" />
        </div>
      </div>
    );
  } 
}

export default Popup;
