import Table from "./index";

class Tenders extends Table{
  row(entry){
    let bidNo = entry.getIn(['planning', 'bidNo']);
    let tender = entry.get('tender');
    let tenderPeriod = tender.get('tenderPeriod');
    let value = tender.get('value');
    return <tr key={bidNo}>
      <td>{bidNo}</td>
      <td>{new Date(tenderPeriod.get('startDate')).toLocaleDateString(undefined, Table.DATE_FORMAT)}</td>
      <td>{new Date(tenderPeriod.get('endDate')).toLocaleDateString(undefined, Table.DATE_FORMAT)}</td>
      <td className="procuring-entity-title">{tender.getIn(['procuringEntity', 'name'])}</td>
      <td>{value.get('amount')} {value.get('currency')}</td>
    </tr>
  }

  render(){
    if(!this.props.data) return null;
    return (
      <table className="table table-striped table-hover tenders-table">
        <thead>
        <tr>
          <th>{this.__("Number")}</th>
          <th>{this.__("Start date")}</th>
          <th>{this.__("End date")}</th>
          <th>{this.__("Procuring entity")}</th>
          <th>{this.__("Estimated value")}</th>
        </tr>
        </thead>
        <tbody>
          {this.props.data.map(this.row)}
        </tbody>
      </table>
    )
  }
}

Tenders.getName = __ => __('Top 10 largest tenders');
Tenders.endpoint = 'topTenLargestTenders';

export default Tenders;