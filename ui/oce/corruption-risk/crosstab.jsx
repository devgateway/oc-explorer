import Table from "../visualizations/tables/index";

class Crosstab extends Table{
  getCustomEP(){
    const {indicators} = this.props;
    return indicators.map(indicator => `flags/${indicator}/crosstab`);
  }

  componentDidUpdate(prevProps, ...args){
    if(this.props.indicators != prevProps.indicators){
      this.fetch();
    }
    super.componentDidUpdate(prevProps, ...args);
  }

  transform(data){
    const {indicators} = this.props;
    let matrix = {}, x = 0, y = 0;
    for(x = 0; x<indicators.length; x++){
      const xIndicatorID = indicators[x];
      matrix[xIndicatorID] = {};
      const datum = data[x][0];
      for(y = 0; y<indicators.length; y++){
        const yIndicatorID = indicators[y];
        if(datum){
          matrix[xIndicatorID][yIndicatorID] = {
            count: datum[yIndicatorID],
            percent: datum.percent[yIndicatorID]
          }
        } else {
          matrix[xIndicatorID][yIndicatorID] = {
            count: 0,
            percent: 0
          }
        }
      }
    }
    return matrix;
  }

  row(rowData, rowIndicatorID){
    const rowIndicatorName = this.t(`crd:indicators:${rowIndicatorID}:name`);
    const rowIndicatorDescription = this.t(`crd:indicators:${rowIndicatorID}:indicator`);
    return (
      <tr key={rowIndicatorID}>
        <td>{rowIndicatorName}</td>
        <td className="nr-flags">{rowData.getIn([rowIndicatorID, 'count'])}</td>
        {rowData.map((datum, indicatorID) => {
           const indicatorName = this.t(`crd:indicators:${indicatorID}:name`);
           const indicatorDescription = this.t(`crd:indicators:${indicatorID}:indicator`);
           if(indicatorID == rowIndicatorID){
             return <td className="not-applicable" key={indicatorID}>&mdash;</td>
           } else {
             const percent = datum.get('percent');
             const count = datum.get('count');
             const color = Math.round(255 - 255 * (percent/100))
             const style = {backgroundColor: `rgb(${color}, 255, ${color})`}
             return (
               <td key={indicatorID} className="hoverable" style={style}>
                 {percent && percent.toFixed(2)} %
                 <div className="crd-popup text-left">
                   <div className="row">
                     <div className="col-sm-12 info">
                       {this.t('crd:corruptionType:crosstab:popup:percents')
                         .replace('$#$', percent.toFixed(2))
                         .replace('$#$', rowIndicatorName)
                         .replace('$#$', indicatorName)}
                     </div>
                     <div className="col-sm-12">
                       <hr/>
                     </div>
                     <div className="col-sm-12 info">
                       <h4>{this.t('crd:corruptionType:crosstab:popup:count').replace('$#$', count)}</h4>
                       <p><strong>{rowIndicatorName}</strong>: {rowIndicatorDescription}</p>
                       <p className="and">{this.t('crd:corruptionType:crosstab:popup:and')}</p>
                       <p><strong>{indicatorName}</strong>: {indicatorDescription}</p>
                     </div>
                   </div>
                   <div className="arrow"/>
                 </div>
               </td>
             )
           }
        }).toArray()}
      </tr>
    )
  }

  render(){
    const {indicators, data} = this.props;
    if(!data) return null;
    if(!data.count()) return null;
    return (
      <table className="table table-striped table-hover table-bordered table-crosstab">
        <thead>
          <tr>
            <th></th>
            <th># Flags</th>
            {data.map((_, indicatorID) => <th key={indicatorID}>{this.t(`crd:indicators:${indicatorID}:name`)}</th>).toArray()}
          </tr>
        </thead>
        <tbody>
          {data.map((rowData, indicatorID) => this.row(rowData, indicatorID)).toArray()}
        </tbody>
      </table>
    )
  }
}

export default Crosstab;
