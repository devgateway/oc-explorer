import Crosstab from './crosstab';
import cn from 'classnames';

class ClickableCrosstab extends Crosstab {
  constructor(...args){
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
           if(indicatorID == rowIndicatorID){
             return <td className="not-applicable" key={indicatorID}>&mdash;</td>
           } else {
             const percent = datum.get('percent');
             const color = Math.round(255 - 255 * (percent/100))
             const style = {backgroundColor: `rgb(${color}, 255, ${color})`}
             const selected = rowIndicatorID == currentlySelected.rowIndicatorID &&
               indicatorID == currentlySelected.indicatorID;
             return (
               <td
                 key={indicatorID}
                 className={cn('selectable', { selected })}
                 style={style}
                 onClick={e => this.setState({ currentlySelected: { rowIndicatorID, indicatorID }})}
               >
                 {percent && percent.toFixed(2)} %
               </td>
             )
           }
        }).toArray()}
      </tr>
    )
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
    document.body.addEventListener('click', () => this.setState({ currentlySelected: false }))
  }

  render(){
    const {indicators, data} = this.props;
    if(!data) return null;
    if(!data.count()) return null;
    return (
      <div onClick={e => { e.stopPropagation() }}>
        {super.render()}
        {this.maybeGetBox()}
      </div>
    )
  }
}

export default ClickableCrosstab;
