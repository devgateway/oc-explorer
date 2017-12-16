import { List } from 'immutable';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import CRDPage from '../page';
import PaginatedTable from '../paginated-table';
import Archive from '../archive';
import { wireProps } from '../tools';

export const mkLink = navigate => (content, { id }) => (
  <a
    href={`#!/crd/procuring-entity/${id}`}
    onClick={() => navigate('procuring-entity', id)}
  >
    {content}
  </a>
);

class PEList extends PaginatedTable {
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
        id: supplier.get('id'),
        name: supplier.get('name')
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
          ID
        </TableHeaderColumn>
        <TableHeaderColumn dataField='name' dataFormat={mkLink(navigate)}>
          Name
        </TableHeaderColumn>
      </BootstrapTable>
    );
  }
}

class ProcuringEntities extends CRDPage {
  render() {
    const { navigate, searchQuery, doSearch } = this.props;
    return (
      <Archive
        {...wireProps(this)}
        searchQuery={searchQuery}
        doSearch={doSearch}
        navigate={navigate}
        className="procuring-entities-page"
        topSearchPlaceholder={this.t('crd:procuringEntities:top-search')}
        List={PEList}
        dataEP="ocds/organization/procuringEntity/all"
        countEP="ocds/organization/procuringEntity/count"
      />
    );
  }
}

export default ProcuringEntities;
