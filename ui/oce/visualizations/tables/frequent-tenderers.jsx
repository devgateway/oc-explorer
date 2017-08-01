import { List } from 'immutable';
import URI from 'urijs';
import Table from './index';
import orgNamesFetching from '../../orgnames-fetching';
import { send, callFunc } from '../../tools';

const maybeSlice = (flag, list) => (flag ? list.slice(0, 10) : list);

class FrequentTenderers extends orgNamesFetching(Table) {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state.showAll = false;
    this.state.winCounts = {};
  }

  row(entry, index) {
    const { winCounts } = this.state;
    const id1 = entry.get('tendererId1');
    const id2 = entry.get('tendererId2');
    return (<tr key={index}>
      <td>{this.getOrgName(id1)}</td>
      <td>{this.getOrgName(id2)}</td>
      <td>{entry.get('pairCount')}</td>
      <td>{winCounts[id1]}</td>
      <td>{winCounts[id2]}</td>
    </tr>);
  }

  getOrgsWithoutNamesIds() {
    const { data } = this.props;
    if (!data) return [];
    return data.map(datum => List([datum.get('tendererId1'), datum.get('tendererId2')]))
      .flatten()
      .filter(id => !this.state.orgNames[id]).toJS();
  }

  getSuppliersWithoutWinCountIds() {
    const { data } = this.props;
    if (!data) return [];
    return data.map(datum => List([datum.get('tendererId1'), datum.get('tendererId2')]))
      .flatten()
      .filter(id => !this.state.winCounts[id]).toJS();
  }

  maybeFetchWinCounts() {
    const idsWithoutWinCounts = this.getSuppliersWithoutWinCountIds();
    if (!idsWithoutWinCounts.length) return;
    send(new URI('/api/activeAwardsCount').addSearch('supplierId', idsWithoutWinCounts))
      .then(callFunc('json'))
      .then((counts) => {
        const winCounts = {};
        counts.forEach(({ supplierId, cnt }) => {
          winCounts[supplierId] = cnt;
        });
        this.setState({ winCounts });
      });
  }

  componentDidMount() {
    super.componentDidMount();
    this.maybeFetchWinCounts();
  }

  componentDidUpdate(prevProps, ...args) {
    super.componentDidUpdate(prevProps, ...args);
    if (prevProps.data !== this.props.data) {
      this.maybeFetchWinCounts();
    }
  }

  render() {
    if (!this.props.data) return null;
    const { showAll } = this.state;
    return (<table className="table table-stripped trable-hover frequent-supplier-bidder-table">
      <thead>
        <tr>
          <th>{this.t('tables:frequentTenderers:supplier')} #1</th>
          <th>{this.t('tables:frequentTenderers:supplier')} #2</th>
          <th>{this.t('tables:frequentTenderers:nrITB')}</th>
          <th>{this.t('tables:frequentTenderers:supplier1wins')}</th>
          <th>{this.t('tables:frequentTenderers:supplier2wins')}</th>
        </tr>
      </thead>
      <tbody>
        {maybeSlice(!showAll, this.props.data).map((entry, index) => this.row(entry, index))}
        {!showAll && this.props.data.count() > 10 && <tr>
          <td colSpan="5">
            <button
              className="btn btn-info btn-danger btn-block"
              onClick={() => this.setState({ showAll: true })}
            >
              {this.t('tables:showAll')}
            </button>
          </td>
        </tr>}
      </tbody>
    </table>);
  }
}

FrequentTenderers.getName = t => t('tables:frequentTenderers:title');
FrequentTenderers.endpoint = 'frequentTenderers';

export default FrequentTenderers;
