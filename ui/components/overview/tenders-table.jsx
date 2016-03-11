import Component from "../pure-render-component";
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
          <h4 className="page-header">Top 10 largest tenders</h4>
          <table className="table tenders-table">
            <thead>
              <tr>
                <th>Number</th>
                <th>Start date</th>
                <th>End date</th>
                <th>Procuring entity</th>
                <th>Estimated price</th>
              </tr>
            </thead>
            <tbody>
              {this.props.data.map(entry => (
                  <tr key={entry.planning.bidNo}>
                    <td>{entry.planning.bidNo}</td>
                    <td>{new Date(entry.tender.tenderPeriod.startDate).toLocaleDateString(undefined, DATE_FORMAT)}</td>
                    <td>{new Date(entry.tender.tenderPeriod.endDate).toLocaleDateString(undefined, DATE_FORMAT)}</td>
                    <td>{entry.tender.procuringEntity.name}</td>
                    <td>{entry.tender.value.amount} {entry.tender.value.currency}</td>
                  </tr>
              ))}
            </tbody>
          </table>
        </section>
    )
  }
}