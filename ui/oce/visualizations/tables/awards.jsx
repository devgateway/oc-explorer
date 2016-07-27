import Table from "./index";
import {pluckImm} from "../../tools";

class Awards extends Table{
  row(entry){
    let bidNo = entry.getIn(['planning', 'bidNo']);
    let awards = entry.get('awards');
    let value = awards.get('value');
    return <tr key={bidNo}>
      <td>{bidNo}</td>
      <td>{new Date(awards.get('date')).toLocaleDateString(undefined, Table.DATE_FORMAT)}</td>
      <td className="supplier-name">
        {awards.get('suppliers').map(pluckImm('name')).join(', ')}
      </td>
      <td>{value.get('amount')} {value.get('currency')}</td>
    </tr>
  }

  render(){
    if(!this.props.data) return null;
    return (
      <table className="table table-striped table-hover awards-table">
        <thead>
        <tr>
          <th>{this.__('Number')}</th>
          <th>{this.__('Date')}</th>
          <th>{this.__('Supplier')}</th>
          <th>{this.__('Value')}</th>
        </tr>
        </thead>
        <tbody>
        {this.props.data.map(this.row)}
        </tbody>
      </table>
    )
  }
}

Awards.getName = __ => __('Top 10 largest awards');
Awards.endpoint = 'topTenLargestAwards';

export default Awards;