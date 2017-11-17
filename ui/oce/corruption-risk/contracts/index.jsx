import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import PaginationList from 'react-bootstrap-table/lib/pagination/PaginationList';
import { List } from 'immutable';
import URI from 'urijs';
// eslint-disable-next-line no-unused-vars
import rbtStyles from 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
import CRDPage from '../page';
import Visualization from '../../visualization';
import TopSearch from './top-search';

const API_ROOT = '/api';

class CList extends Visualization {
  constructor(...args){
    super(...args);
    this.state = {
      pageSize: 20,
      page: 1
    }
  }

  buildUrl(ep) {
    const { filters, searchQuery } = this.props;
    const { pageSize, page } = this.state;
    const skip = pageSize * (page - 1);
    const uri = new URI(`${API_ROOT}/${ep}`)
      .addSearch('pageSize', pageSize)
      .addSearch('pageNumber', page - 1)
      .addSearch(filters.toJS());
    return searchQuery ?
      uri.addSearch('text', searchQuery) :
      uri;
  }

  componentDidUpdate(prevProps, prevState) {
    const propsChanged = ['filters', 'searchQuery'].some(key => this.props[key] != prevProps[key]);
    const stateChanged = ['pageSize', 'page'].some(key => this.state[key] != prevState[key]);
    if (propsChanged || stateChanged) {
      this.fetch();
    }
  }

  mkLink(content, { id }) {
    const { navigate } = this.props;
    return (
      <a
        href="javascript:void(0);"
        onClick={() => navigate('contract', id)}
      >
        {content}
      </a>
    );
  }

  render() {
    const { data, count } = this.props;

    if (!data || !data.count()) return null;

    const { pageSize, page } = this.state;

    const jsData = data.map((contract) => {
      const tenderAmount = contract.getIn(['tender', 'value', 'amount'], 'N/A') +
          ' ' +
          contract.getIn(['tender', 'value', 'currency'], '');

      const winningAward = contract.get('awards', List()).find(award => award.get('status') === 'active');
      let awardAmount = 'N/A';
      if (winningAward) {
        awardAmount = winningAward.getIn(['value', 'amount'], 'N/A') +
          ' ' +
          winningAward.getIn(['value', 'currency'], '')
      }

      const startDate = contract.getIn(['tender', 'tenderPeriod', 'startDate']);

      const flagTypes = contract.getIn(['flags', 'flaggedStats'], List())
        .map(flagType => this.t(`crd:corruptionType:${flagType.get('type')}:name`))
        .join(', ') || 'N/A';

      return {
        status: contract.getIn(['tender', 'status'], 'N/A'),
        id: contract.get('ocid'),
        title: contract.getIn(['tender', 'title'], 'N/A'),
        PEName: contract.getIn(['tender', 'procuringEntity', 'name'], 'N/A'),
        tenderAmount,
        awardAmount,
        startDate: startDate ? new Date(startDate).toLocaleDateString() : 'N/A',
        flagTypes,
      };
    }).toJS();

    return (
      <BootstrapTable
        data={jsData}
        striped
        bordered={false}
        pagination
        remote
        fetchInfo = {{
          dataTotalSize: count
        }}
        options={{
          page,
          onPageChange: page => this.setState({ page }),
          sizePerPage: pageSize,
          sizePerPageList: [20, 50, 100, 200].map(value => ({text: value, value})),
          onSizePerPageList: pageSize => this.setState({ pageSize }),
          paginationPosition: 'both',
        }}
      >
        <TableHeaderColumn dataField="status">
          {this.t('crd:contracts:baseInfo:status')}
        </TableHeaderColumn>

        <TableHeaderColumn isKey dataField="id" dataFormat={this.mkLink.bind(this)}>
          {this.t('crd:procurementsTable:contractID')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="title" dataFormat={this.mkLink.bind(this)}>
          {this.t('crd:general:contract:title')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="PEName">
          {this.t('crd:contracts:list:procuringEntity')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="tenderAmount">
          {this.t('crd:procurementsTable:tenderAmount')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="awardAmount">
          {this.t('crd:contracts:list:awardAmount')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="startDate">
          {this.t('crd:procurementsTable:tenderDate')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="flagTypes">
          {this.t('crd:procurementsTable:flagType')}
        </TableHeaderColumn>
      </BootstrapTable>
    )
  }
}

CList.endpoint = 'flaggedRelease/all';

export default class Contracts extends CRDPage {
  constructor(...args) {
    super(...args);
    this.state = {
      list: List(),
    };
  }

  render() {
    const { list } = this.state;
    const { filters, navigate, translations, searchQuery, doSearch, count } = this.props;
    return (
      <div className="contracts-page">
        <TopSearch
          translations={translations}
          searchQuery={searchQuery}
          doSearch={doSearch}
        />

        {searchQuery && <h3 className="page-header">
          {this.t('crd:contracts:top-search:resultsFor').replace('$#$', searchQuery)}
        </h3>}

        <CList
          data={list}
          filters={filters}
          requestNewData={(_, newList) => this.setState({ list: newList })}
          navigate={navigate}
          translations={translations}
          searchQuery={searchQuery}
          count={count}
        />

        {searchQuery && !list.count() ? <strong>{this.t('crd:contracts:top-search:nothingFound')}</strong> : null}
      </div>
    );
  }
}
