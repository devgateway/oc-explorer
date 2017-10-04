import CRDPage from './page';
import Visualization from '../visualization';
import { List } from 'immutable';

class CList extends Visualization {
  render() {
    const { data, navigate } = this.props;
    return (
      <div>
        {data.map(contract => {
           const id = contract.get('ocid');
           return (
             <p>
               <a
                 href="javascript:void(0);"
                 onClick={() => navigate('contract', id)}
                 key={id}
               >
                 {contract.getIn(['tender', 'title'], id)}
               </a>
             </p>
           )
        })}
      </div>
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
      <CList
        data={list}
        filters={filters}
        requestNewData = {(_, list) => this.setState({list})}
        navigate={navigate}
      />
    );
  }
}
