import translatable from '../../../../../translatable';

class Popup extends translatable(React.PureComponent) {
  render() {
    const { coordinate, active, viewBox, payload } = this.props;
    if (!active || !payload[0]) return null;

    const { name, wins, flags } = payload[0].payload;

    let POPUP_HEIGHT = 70;
    if (name.length > 100) {
      POPUP_HEIGHT = 140;
    } else if (name.length > 70 ) {
      POPUP_HEIGHT = 110;
    } else if (name.length > 40 ) {
      POPUP_HEIGHT = 90;
    }

    const style = {
      left: 0,
      top: coordinate.y - POPUP_HEIGHT - viewBox.top - 4,
      width: 300,
      height: POPUP_HEIGHT,
    }

    const winLabel = wins === 1 ?
      this.t('crd:supplier:win:sg') :
      this.t('crd:supplier:win:pl');

    const flagLabel = flags === 1 ?
      this.t('crd:contracts:baseInfo:flag:sg') :
      this.t('crd:contracts:baseInfo:flag:pl');

    return (
      <div>
        <div
          className="crd-popup donut-popup text-center"
          style={style}
        >
          {name}
          <br />
          {wins} {winLabel}, {flags} {flagLabel}
          <div className="arrow" />
        </div>
      </div>
    );
  } 
}

export default Popup;
