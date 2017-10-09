import { List } from 'immutable';
import CRDPage from '../page';
import Visualization from '../../visualization';

class CList extends Visualization {
  render() {
    const { data, navigate } = this.props;
    return (
      <tbody>
        {data.map((contract) => {
           const id = contract.get('ocid');
           const startDate = contract.getIn(['tender', 'tenderPeriod', 'startDate']);
           const flagType = contract.get('flags').toList().getIn([0, 'types', 0]);
           return (
             <tr key={id}>
               <td>{contract.getIn(['tender', 'status'], this.t('general:undefined'))}</td>
               <td>
                 <a
                   href="javascript:void(0);"
                   onClick={() => navigate('contract', this.t('general:undefined'))}
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
                 {this.t(`crd:corruptionType:${flagType}:name`)}
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
    const { filters, navigate, translations } = this.props;
    return (
      <div className="contracts-page">
        <table className="table table-striped">
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
          />
        </table>
      </div>
    );
  }
}
