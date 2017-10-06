import CRDPage from '../page';
import Visualization from '../../visualization';
import { List } from 'immutable';

class CList extends Visualization {
  render() {
    const { data, navigate } = this.props;
    return (
      <tbody>
        {data.map(contract => {
           const id = contract.get('ocid');
console.log(contract.toJS());
           return (
             <tr>
               <td>{data.getIn(['tender', 'status'])}</td>
               <td>
                 <a
                   href="javascript:void(0);"
                   onClick={() => navigate('contract', id)}
                   key={id}
                 >
                   {contract.getIn(['tender', 'title'], id)}
                 </a>
               </td>
             </tr>
           )
        })}
      </tbody>
    );
  }
}

CList.endpoint = 'ocds/release/all';

export default class Contracts extends CRDPage {
  constructor(...args){
    super(...args);
    this.state = {
      list: List()
    }
  }

  render() {
    const { list } = this.state;
    const { filters, navigate } = this.props;
    return (
      <div id="contracts-page">
        <table className="table">
          <thead>
            <th>{this.t('crd:contracts:baseInfo:status')}</th>
            <th>{this.t('crd:procurementsTable:contractID')}</th>
            <th>{this.t('crd:general:contract:title')}</th>
            <th>{this.t('crd:contracts:list:procuringEntity')}</th>
            <th>{this.t('crd:procurementsTable:tenderAmount')}</th>
            <th>{this.t('crd:procurementsTable:awardAmount')}</th>
            <th>{this.t('crd:procurementsTable:tenderDate')}</th>
            <th>{this.t('crd:procurementsTable:flagType')}</th>
          </thead>
          <CList
            data={list}
            filters={filters}
            requestNewData = {(_, list) => this.setState({list})}
            navigate={navigate}
          />
        </table>
      </div>
    );
  }
}
