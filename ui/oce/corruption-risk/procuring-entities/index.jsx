import { List } from 'immutable';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import CRDPage from '../page';
import PaginatedTable from '../paginated-table';
import Archive from '../archive';
import { wireProps } from '../tools';
import { PEIds, TendersCount, AwardsCount } from './state';

export const mkLink = navigate => (content, { id }) => (
  <a
    href={`#!/crd/procuring-entity/${id}`}
    onClick={() => navigate('procuring-entity', id)}
  >
    {content}
  </a>
);

class PEList extends PaginatedTable {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.tenders = {};
    this.state.awards = {};
  }

  getCustomEP() {
    const { searchQuery } = this.props;
    const eps = super.getCustomEP();
    return searchQuery ?
      eps.map(ep => ep.addSearch('text', searchQuery)) :
      eps;
  }

  componentWillMount() {
    TendersCount.addListener(
      'PEList',
      this.updateBindings.bind(this),
    );
    AwardsCount.addListener(
      'PEList',
      this.updateBindings.bind(this),
    );
  }

  updateBindings() {
    Promise.all([
      TendersCount.getState(),
      AwardsCount.getState(),
    ]).then(([tenders, awards]) => {
      this.setState({ tenders, awards });
    });
  }

  componentWillUnmount() {
    TendersCount.removeListener('PEList');
    AwardsCount.removeListener('PEList');
  }

  componentDidUpdate(prevProps, prevState) {
    const propsChanged = ['filters', 'searchQuery'].some(key => this.props[key] !== prevProps[key]);
    if (propsChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps, prevState);
    }

    const { data } = this.props;
    if (prevProps.data !== data) {
      PEIds.assign(
        'PEList',
        this.props.data
          .get('data', List())
          .map(datum => datum.get('procuringEntityId'))
      );
    }
  }

  render() {
    const { data, navigate } = this.props;

    const count = data.get('count', 0);

    const { pageSize, page, tenders, awards } = this.state;

    const jsData = data.get('data', List()).map((supplier) => {
      const id = supplier.get('procuringEntityId');
      return {
        id,
        name: supplier.get('procuringEntityName'),
        nrTenders: tenders[id],
        nrAwards: awards[id],
        nrFlags: supplier.get('countFlags'),
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
        <TableHeaderColumn dataField='id' isKey dataFormat={mkLink(navigate)}>
          {this.t('crd:suppliers:ID')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField='name' dataFormat={mkLink(navigate)}>
          {this.t('crd:suppliers:name')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField='nrTenders'>
          {this.t('crd:procuringEntities:noOfTenders')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField='nrAwards'>
          {this.t('crd:procuringEntities:noOfAwards')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField='nrFlags'>
          {this.t('crd:procurementsTable:noOfFlags')}
        </TableHeaderColumn>
      </BootstrapTable>
    );
  }
}

class ProcuringEntities extends CRDPage {
  requestNewData(path, newData) {
    this.props.requestNewData(
      path,
      newData.set('count', newData.getIn(['count', 0, 'count'], 0))
    );
  }

  render() {
    const { navigate, searchQuery, doSearch } = this.props;
    return (
      <Archive
        {...wireProps(this)}
        requestNewData={this.requestNewData.bind(this)}
        searchQuery={searchQuery}
        doSearch={doSearch}
        navigate={navigate}
        className="procuring-entities-page"
        topSearchPlaceholder={this.t('crd:procuringEntities:top-search')}
        List={PEList}
        dataEP="procuringEntitiesByFlags"
        countEP="procuringEntitiesByFlags/count"
      />
    );
  }
}

export default ProcuringEntities;
