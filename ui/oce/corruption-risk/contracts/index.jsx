import { List } from 'immutable';
import cn from 'classnames';
import URI from 'urijs';
import CRDPage from '../page';
import Visualization from '../../visualization';
import TopSearch from './top-search';

const API_ROOT = '/api';

class CList extends Visualization {
  buildUrl(ep) {
    let { filters, searchQuery } = this.props;
    let uri = new URI(API_ROOT + '/' + ep).addSearch(filters.toJS());
    return searchQuery ?
      uri.addSearch('text', searchQuery) :
      uri;
  }

  componentDidUpdate(prevProps){
    const { filters, searchQuery } = this.props;
    if (filters != prevProps.filters || searchQuery != prevProps.searchQuery) {
      this.fetch();
    }
  }

  render() {
    const { data, navigate } = this.props;
    return (
      <tbody>
        {data.map((contract) => {
           const id = contract.get('ocid');
           const startDate = contract.getIn(['tender', 'tenderPeriod', 'startDate']);
           const flagType = contract.getIn(['flags', 'flaggedStats', 0, 'type']);
           return (
             <tr key={id}>
               <td>{contract.getIn(['tender', 'status'], this.t('general:undefined'))}</td>
               <td>
                 <a
                   href="javascript:void(0);"
                   onClick={() => navigate('contract', id)}
                 >
                   {id}
                 </a>
               </td>
               <td>
                 <a
                   href="javascript:void(0);"
                   onClick={() => navigate('contract', id)}
                 >
                   {contract.getIn(['tender', 'title'], id)}
                 </a>
               </td>
               <td>
                 {contract.getIn(['tender', 'procuringEntity', 'name'], this.t('general:undefined'))}
               </td>
               <td>
                 {contract.getIn(['tender', 'value', 'amount'], this.t('general:undefined'))}
                 &nbsp;
                 {contract.getIn(['tender', 'value', 'currency'])}
               </td>
               <td>
                 {contract.getIn(['awards', 0, 'value', 'amount'], this.t('general:undefined'))}
                 &nbsp;
                 {contract.getIn(['awards', 0, 'value', 'currency'])}
               </td>
               <td>
                 {startDate ?
                   new Date(startDate).toLocaleDateString() :
                   this.t('general:undefined')
                 }
               </td>
               <td>
                 {flagType ?
                   this.t(`crd:corruptionType:${flagType}:name`) :
                   this.t('general:undefined')}
               </td>
             </tr>
           );
        })}
      </tbody>
    );
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

        <table className={cn('table', 'table-striped', {hide: !list.count()})}>
          <thead>
            <tr>
              <th>{this.t('crd:contracts:baseInfo:status')}</th>
              <th>{this.t('crd:procurementsTable:contractID')}</th>
              <th>{this.t('crd:general:contract:title')}</th>
              <th>{this.t('crd:contracts:list:procuringEntity')}</th>
              <th>{this.t('crd:procurementsTable:tenderAmount')}</th>
              <th>{this.t('crd:contracts:list:awardAmount')}</th>
              <th>{this.t('crd:procurementsTable:tenderDate')}</th>
              <th>{this.t('crd:procurementsTable:flagType')}</th>
            </tr>
          </thead>
          <CList
            data={list}
            filters={filters}
            requestNewData={(_, newList) => this.setState({ list: newList })}
            navigate={navigate}
            translations={translations}
            searchQuery={searchQuery}
          />
        </table>

        {searchQuery && !list.count() ? <strong>{this.t('crd:contracts:top-search:nothingFound')}</strong> : null}
      </div>
    );
  }
}
