import URI from 'urijs';
import { List, Map } from 'immutable';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import CRDPage from '../page';
import PaginatedTable from '../paginated-table';
import Archive from '../archive';
import { wireProps } from '../tools';
import { fetchEP, pluckImm, cacheFn } from '../../tools';

export const mkLink = navigate => (content, { id }) => (
  <a
    href={`#!/crd/supplier/${id}`}
    onClick={() => navigate('supplier', id)}
  >
    {content}
  </a>
);

class SList extends PaginatedTable {
  getCustomEP() {
    const { searchQuery } = this.props;
    const eps = super.getCustomEP();
    return searchQuery ?
      eps.map(ep => ep.addSearch('text', searchQuery)) :
      eps;
  }

  componentDidUpdate(prevProps, prevState) {
    const propsChanged = ['filters', 'searchQuery'].some(key => this.props[key] !== prevProps[key]);
    if (propsChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps, prevState);
    }
  }

  render() {
    const { data, navigate } = this.props;

    const count = data.get('count', 0);

    const { pageSize, page } = this.state;

    const jsData = data.get('data', List()).map((supplier) => {
      return {
        id: supplier.get('supplierId'),
        name: supplier.get('supplierName'),
        wins: supplier.get('wins'),
        winAmount: supplier.get('winAmount'),
        losses: supplier.get('losses'),
        flags: supplier.get('countFlags'),
      }
    }).toJS();

    return (
      <BootstrapTable
        data={jsData}
        striped
        bordered={false}
        pagination
        remote
        fetchInfo={{
          dataTotalSize: count,
        }}
        options={{
          page,
          onPageChange: newPage => this.setState({ page: newPage }),
          sizePerPage: pageSize,
          sizePerPageList: [20, 50, 100, 200].map(value => ({ text: value, value })),
          onSizePerPageList: newPageSize => this.setState({ pageSize: newPageSize }),
          paginationPosition: 'both',
        }}
      >
        <TableHeaderColumn dataField="name" dataFormat={mkLink(navigate)}>
          {this.t('crd:suppliers:name')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField="id" isKey dataFormat={mkLink(navigate)}>
          {this.t('crd:suppliers:ID')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField="wins">
          {this.t('crd:suppliers:wins')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField="winAmount">
          {this.t('crd:suppliers:totalWon')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField="losses">
          {this.t('crd:suppliers:losses')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField='flags'>
          {this.t('crd:suppliers:nrFlags')}
        </TableHeaderColumn>
      </BootstrapTable>
    );
  }
}

class Suppliers extends CRDPage {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.winLossFlagInfo = Map();
    this.injectWinLossData = cacheFn((data, winLossFlagInfo) => {
      return data.update('data', List(), list => list.map(supplier => {
        const id = supplier.get('supplierId');
        if (!winLossFlagInfo.has(id)) return supplier;
        const info = winLossFlagInfo.get(id);
        return supplier
          .set('wins', info.won.count)
          .set('winAmount', info.won.totalAmount)
          .set('losses', info.lostCount)
          .set('flags', info.applied.countFlags)
      }))
    });
  }

  onNewDataRequested(path, newData) {
    const supplierIds = newData.get('data').map(pluckImm('supplierId'));
    this.setState({ winLossFlagInfo: Map() });
    fetchEP(new URI('/api/procurementsWonLost').addSearch({
      bidderId: supplierIds.toJS(),
    })).then(result => {
      this.setState({
        winLossFlagInfo: Map(result.map(datum => [
          datum.applied._id,
          datum
        ]))
      });
    });
    this.props.requestNewData(
      path,
      newData.set('count', newData.getIn(['count', 0, 'count'], 0))
    );
  }

  render() {
    const { navigate, searchQuery, doSearch, data } = this.props;
    const { winLossFlagInfo } = this.state;
    return (
      <Archive
        {...wireProps(this)}
        data={this.injectWinLossData(data, winLossFlagInfo)}
        requestNewData={this.onNewDataRequested.bind(this)}
        searchQuery={searchQuery}
        doSearch={doSearch}
        navigate={navigate}
        className="suppliers-page"
        topSearchPlaceholder={this.t('crd:suppliers:top-search')}
        List={SList}
        dataEP="suppliersByFlags"
        countEP="suppliersByFlags/count"
      />
    );
  }
}

export default Suppliers;
