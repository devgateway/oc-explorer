import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import { List } from 'immutable';
import URI from 'urijs';
// eslint-disable-next-line no-unused-vars
import rbtStyles from 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
import CRDPage from '../page';
import Visualization from '../../visualization';
import TopSearch from './top-search';

const API_ROOT = '/api';

class CList extends Visualization {
  buildUrl(ep) {
    const { filters, searchQuery } = this.props;
    const uri = new URI(`${API_ROOT}/${ep}`)
      .addSearch('pageSize', 20)
      .addSearch(filters.toJS());
    return searchQuery ?
      uri.addSearch('text', searchQuery) :
      uri;
  }

  componentDidUpdate(prevProps) {
    const { filters, searchQuery } = this.props;
    if (filters !== prevProps.filters || searchQuery !== prevProps.searchQuery) {
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
    const { data } = this.props;
    if (!data || !data.count()) return null;
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
        options={{
          page: 1,
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
    const { filters, navigate, translations, searchQuery, doSearch } = this.props;
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
        />

        {searchQuery && !list.count() ? <strong>{this.t('crd:contracts:top-search:nothingFound')}</strong> : null}
      </div>
    );
  }
}
