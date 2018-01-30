import cn from 'classnames';
import Crosstab from '../crosstab';
import styles from './style.less';
import { colorLuminance } from '../tools';

class ClickableCrosstab extends Crosstab {
  constructor(...args) {
    super(...args);
    this.state.currentlySelected = false;
  }

  row(rowData, rowIndicatorID) {
    const { currentlySelected } = this.state;
    const rowIndicatorName = this.t(`crd:indicators:${rowIndicatorID}:name`);
    return (
      <tr key={rowIndicatorID}>
        <td>{rowIndicatorName}</td>
        <td className="nr-flags">{rowData.getIn([rowIndicatorID, 'count'])}</td>
        {rowData.map((datum, indicatorID) => {
          if(indicatorID === rowIndicatorID) {
            return <td className="not-applicable" key={indicatorID}>&mdash;</td>;
          } else {
            const { showRawNumbers } = this.props;
            const count = datum.get('count', 0);
            const percent = datum.get('percent', 0);
            const color = colorLuminance('#00ff00', percent / 100 - .5);
            const style = { backgroundColor: color };
            const selected = rowIndicatorID === currentlySelected.rowIndicatorID &&
              indicatorID === currentlySelected.indicatorID;

            return (
              <td
                key={indicatorID}
                className={cn('selectable', { selected })}
                style={style}
                onClick={() => this.setState({ currentlySelected: { rowIndicatorID, indicatorID }})}
              >
                { showRawNumbers ?
                  `${count} (${percent.toFixed(2)} %)` :
                  `${percent.toFixed(2)} %`}
              </td>
            );
          }
        }).toArray()}
      </tr>
    );
  }

  maybeGetBox() {
    const { currentlySelected } = this.state;
    if (!currentlySelected) return null;
    const { rowIndicatorID, indicatorID } = currentlySelected;

    const row = this.props.data.get(rowIndicatorID);
    const datum = row.get(indicatorID);

    const rowIndicatorName = this.t(`crd:indicators:${rowIndicatorID}:name`);
    const rowIndicatorDescription = this.t(`crd:indicators:${rowIndicatorID}:indicator`);
    const indicatorDescription = this.t(`crd:indicators:${indicatorID}:indicator`);
    const indicatorName = this.t(`crd:indicators:${indicatorID}:name`);
    const count = datum.get('count');
    const percent = datum.get('percent');

    return (
      <div className="crosstab-box text-left">
        <div className="row">
          <div className="col-sm-12 title">
            {this.t('crd:corruptionType:crosstab:popup:percents')
              .replace('$#$', percent.toFixed(2))
              .replace('$#$', rowIndicatorName)
              .replace('$#$', indicatorName)}
          </div>
          <div className="col-sm-12">
            <h5>{this.t('crd:corruptionType:crosstab:popup:count').replace('$#$', count)}</h5>
            <p><strong>{rowIndicatorName}</strong>: {rowIndicatorDescription}</p>
            <p><strong>{indicatorName}</strong>: {indicatorDescription}</p>
          </div>
        </div>
      </div>
    );
  }

  componentDidMount() {
    super.componentDidMount();
    document.body.addEventListener('click', () => this.setState({ currentlySelected: false }));
  }

  render() {
    const { data } = this.props;
    if (!data) return null;
    if (!data.count()) return null;
    return (
      <div onClick={(e) => { e.stopPropagation(); }} className="clickable-crosstab">
        {super.render()}
        {this.maybeGetBox()}
      </div>
    );
  }
}

export default ClickableCrosstab;
