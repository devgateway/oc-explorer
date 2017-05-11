import cn from "classnames";
import Table from "../visualizations/tables";

class ProcurementsTable extends Table{
  row(entry, index){
    const tenderValue = entry.getIn(['tender', 'value']);
    const awardValue = entry.getIn(['awards', 0, 'value']);
    const tenderPeriod = entry.get('tenderPeriod');
    const startDate = new Date(tenderPeriod.get('startDate'));
    const endDate = new Date(tenderPeriod.get('endDate'));
    const flags = entry.get('flags');
    const flaggedStats = flags.get('flaggedStats');
    const type = flaggedStats.get('type');
    const flagIds =
      flags
        .filter(
          flag => flag.has && flag.has('types') && flag.get('types').includes(type) && flag.get('value')
        )
        .keySeq();

    const procuringEntityName = entry.getIn(['procuringEntity', 'name']);
    const title = entry.get('title');

    return (
      <tr key={index}>
        <td>{entry.get('tag', []).join(', ')}</td>
        <td>{entry.get('ocid')}</td>
        <td>
          <div className="oce-3-line-text" title={title}>
            {title}
          </div>
        </td>
        <td>
          <div title={procuringEntityName} className="oce-3-line-text">
            {procuringEntityName}
          </div>
        </td>
        <td>{tenderValue && tenderValue.get('amount')} {tenderValue && tenderValue.get('currency')}</td>
        <td>{awardValue.get('amount')} {awardValue.get('currency')}</td>
        <td>{startDate.toLocaleDateString()}&mdash;{endDate.toLocaleDateString()}</td>
        <td>{type}</td>
        <td className="hoverable popup-left">
          {flaggedStats.get('count')}
          <div className="crd-popup text-center">
            <div className="row">
              <div className="col-sm-12 info">
                <h5>Associated {type[0] + type.substr(1).toLowerCase()} Flags</h5>
              </div>
              <div className="col-sm-12">
                <hr/>
              </div>
              <div className="col-sm-12 info">
                {flagIds.map(flagId => <p key={flagId}>{this.t(`crd:indicators:${flagId}:name`)}</p>)}
              </div>
            </div>
            <div className="arrow"/>
          </div>
        </td>
      </tr>
    )
  }

  render(){
    const {data} = this.props;
    return (
      <table className={`table table-striped table-hover ${this.getClassName()}`}>
        <thead>
          <tr>
            <th>Status</th>
            <th>Contract ID</th>
            <th>Title</th>
            <th>Procuring Entity</th>
            <th>Tender Amount</th>
            <th>Awards Amount</th>
            <th>Tender Date</th>
            <th className="flag-type">Flag Type</th>
            <th>No. of Flags</th>
          </tr>
        </thead>
        <tbody>
          {data && data.map(this.row.bind(this))}
        </tbody>
      </table>
    )
  }
}

export default ProcurementsTable;
