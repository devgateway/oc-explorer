import Component from "../pure-render-component";
import {pluck} from "../../tools";
const DATE_FORMAT = {
  year: 'numeric',
  month: 'short',
  day: 'numeric'
};

export default class TendersTable extends Component{
  render(){
    if(!this.props.data) return null;
    return (
        <section>
          <h4 className="page-header">Top 10 largest awards</h4>
          <table className="table tenders-table">
            <thead>
              <tr>
                <th>Number</th>
                <th>Price</th>
                <th>Supplier</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {this.props.data.map(entry => (
                  <tr key={entry.planning.bidNo}>
                    <td>{entry.planning.bidNo}</td>
                    <td>{entry.awards.value.amount} {entry.awards.value.currency}</td>
                    <td>
                      {entry.awards.suppliers.map(pluck('name')).join(', ')}
                    </td>
                    <td>{new Date(entry.awards.date).toLocaleDateString(undefined, DATE_FORMAT)}</td>
                  </tr>
              ))}
            </tbody>
          </table>
        </section>
    )
  }
}