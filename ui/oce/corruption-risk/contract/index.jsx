import CRDPage from '../page';
import Visualization from '../../visualization';
import { Map, List } from 'immutable';
import translatable from '../../translatable';
import styles from './style.less';

class Info extends translatable(Visualization) {
  getCustomEP(){
    const { id } = this.props;
    return `ocds/release/ocid/${id}`;
  }

  render() {
    const { data } = this.props;
    const title = data.getIn(['tender', 'title']);
    const suppliers = data.get('awards', List()).flatMap(award => award.get('suppliers'));
    const startDate = data.getIn(['tender', 'tenderPeriod', 'startDate']);
    const endDate = data.getIn(['tender', 'tenderPeriod', 'endDate']);
    console.log(data.toJS());
    return (
      <section>
        <dl>
          <dt>{this.t('crd:procurementsTable:contractID')}</dt>
          <dd>{data.get('ocid')}</dd>
          {title && <dt>{this.t('crd:general:contract:title')}</dt>}
          {title && <dd>{title}</dd>}
        </dl>
        <table className="table table-bordered">
          <tbody>
            <tr>
              <td>
                <dl>
                  <dt>Procuring entity name</dt>
                  <dd>{data.getIn(['tender', 'procuringEntity', 'name'], this.t('general:undefined'))}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>Buyer</dt>
                  <dd>{data.getIn(['buyer', 'name'], this.t('general:undefined'))}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>Suppliers</dt>
                  <dd>
                    {suppliers.count() ?
                      suppliers.map(supplier => <p>{supplier.get('name')}</p>) :
                      this.t('general:undefined')
                    }
                  </dd>
                </dl>
              </td>
            </tr>
            <tr>
              <td>
                <dl>
                  <dt>Status</dt>
                  <dd>{data.getIn(['tender', 'status'], this.t('general:undefined'))}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>Amounts</dt>
                  <dd>
                    {data.getIn(['tender', 'value', 'amount'], this.t('general:undefined'))}
                    &nbsp;
                    {data.getIn(['tender', 'value', 'currency'])}
                  </dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>Dates</dt>
                  <dd>
                    {startDate &&
                      new Date(startDate).toLocaleDateString()
                    }
                    {startDate && endDate ? <span>&ndash;</span> : this.t('general:undefined')}
                    {startDate &&
                      new Date(endDate).toLocaleDateString()
                    }
                  </dd>
                </dl>
              </td>
            </tr>
          </tbody>
        </table>
      </section>
    );
  }
}

export default class Contract extends CRDPage {
  constructor(...args){
    super(...args);
    this.state = {
      contract: Map()
    }
  }

  render() {
    const { contract } = this.state;
    const { id, translations } = this.props;
    return (
      <div className="contract-page">
        <Info
          id={id}
          data={contract}
          filters={Map()}
          requestNewData={(_, contract) => this.setState({contract})}
          translations={translations}
        />
      </div>
    );
  }
}
