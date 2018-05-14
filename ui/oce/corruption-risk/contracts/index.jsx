import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import { List } from 'immutable';
// eslint-disable-next-line no-unused-vars
import rbtStyles from 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
import CRDPage from '../page';
import { getAwardAmount, mkContractLink, wireProps, _3LineText } from '../tools';
import PaginatedTable from '../paginated-table';
import Archive from '../archive';
import BackendDateFilterable from '../backend-date-filterable';

class CList extends PaginatedTable {
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

    if (!data) return null;

    const contracts = data.get('data', List());
    const count = data.get('count', 0);

    const { pageSize, page } = this.state;

    const jsData = contracts.map((contract) => {
      const tenderAmount = contract.getIn(['tender', 'value', 'amount'], 'N/A') +
          ' ' +
          contract.getIn(['tender', 'value', 'currency'], '');

      const startDate = contract.getIn(['tender', 'tenderPeriod', 'startDate']);

      const flags = contract.get('flags');

      const flagTypes = flags.get('laggedStats', List())
        .map(flagType => this.t(`crd:corruptionType:${flagType.get('type')}:name`))
        .join(', ') || 'N/A';

      return {
        status: contract.getIn(['tender', 'status'], 'N/A'),
        id: contract.get('ocid'),
        title: contract.getIn(['tender', 'title'], 'N/A'),
        PEName: contract.getIn(['tender', 'procuringEntity', 'name'], 'N/A'),
        tenderAmount,
        awardAmount: getAwardAmount(contract),
        startDate: startDate ? new Date(startDate).toLocaleDateString() : 'N/A',
        flagTypes,
        nrFlags: flags.get('totalFlagged'),
      };
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
        <TableHeaderColumn dataField="status">
          {this.t('crd:contracts:baseInfo:status')}
        </TableHeaderColumn>

        <TableHeaderColumn
          isKey
          dataField="id"
          dataFormat={mkContractLink(navigate)}
          className="ocid"
          columnClassName="ocid"
        >
          {this.t('crd:procurementsTable:contractID')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="title" dataFormat={mkContractLink(navigate)}>
          {this.t('crd:general:contract:title')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="PEName" dataFormat={_3LineText}>
          {this.t('crd:contracts:list:procuringEntity')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="tenderAmount">
          {this.t('crd:procurementsTable:tenderAmount')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="awardAmount">
          {this.t('crd:contracts:list:awardAmount')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="startDate" className="date" columnClassName="date">
          {this.t('crd:procurementsTable:tenderDate')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="flagTypes">
          {this.t('crd:procurementsTable:flagType')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="nrFlags">
          {this.t('crd:procurementsTable:noOfFlags')}
        </TableHeaderColumn>
      </BootstrapTable>
    );
  }
}

export default class Contracts extends CRDPage {
  render() {
    const { searchQuery, doSearch, navigate } = this.props;
    return (
      <BackendDateFilterable
        {...wireProps(this)}
      >
        <Archive
          searchQuery={searchQuery}
          doSearch={doSearch}
          navigate={navigate}
          className="contracts-page"
          topSearchPlaceholder={this.t('crd:contracts:top-search')}
          List={CList}
          dataEP="flaggedRelease/all"
          countEP="flaggedRelease/count"
        />
      </BackendDateFilterable>
    );
  }
}
