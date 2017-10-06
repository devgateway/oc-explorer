import CRDPage from '../page';
import Visualization from '../../visualization';
import { Map, List } from 'immutable';
import translatable from '../../translatable';
import styles from './style.less';

class Info extends translatable(Visualization) {
  constructor(...args){
    super(...args);
    this.state.showAllSuppliers = false;
  }

  getCustomEP(){
    const { id } = this.props;
    return `ocds/release/ocid/${id}`;
  }

  getSuppliers(){
    const { data } = this.props;
    const { showAllSuppliers } = this.state;
    const suppliers = data.get('awards', List()).flatMap(award => award.get('suppliers'));
    return showAllSuppliers ?
      suppliers :
      suppliers.slice(0, 2);
  }

  render() {
    const { data } = this.props;
    const title = data.getIn(['tender', 'title']);
    const suppliers = data.get('awards', List()).flatMap(award => award.get('suppliers'));
    const startDate = data.getIn(['tender', 'tenderPeriod', 'startDate']);
    const endDate = data.getIn(['tender', 'tenderPeriod', 'endDate']);
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
                  <dt>{this.t('crd:contracts:baseInfo:procuringEntityName')}</dt>
                  <dd>{data.getIn(['tender', 'procuringEntity', 'name'], this.t('general:undefined'))}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>{this.t('crd:contracts:baseInfo:buyer')}</dt>
                  <dd>{data.getIn(['buyer', 'name'], this.t('general:undefined'))}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>{this.t('crd:contracts:baseInfo:suppliers')}</dt>
                  <dd>
                    {suppliers.count() ?
                      this.getSuppliers().map(supplier => <p>{supplier.get('name')}</p>) :
                      this.t('general:undefined')
                    }
                  </dd>
                  <a href="javascript:void(0);" onClick={() => this.setState({showAllSuppliers: true})}></a>
                </dl>
              </td>
            </tr>
            <tr>
              <td>
                <dl>
                  <dt>{this.t('crd:contracts:baseInfo:status')}</dt>
                  <dd>{data.getIn(['tender', 'status'], this.t('general:undefined'))}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>{this.t('crd:contracts:baseInfo:amounts')}</dt>
                  <dd>
                    {data.getIn(['tender', 'value', 'amount'], this.t('general:undefined'))}
                    &nbsp;
                    {data.getIn(['tender', 'value', 'currency'])}
                  </dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>{this.t('crd:contracts:baseInfo:dates')}</dt>
                  <dd>
                    {startDate &&
                      new Date(startDate).toLocaleDateString()
                    }
                    {startDate && endDate ? <span>&ndash;</span> : this.t('general:undefined')}
                    {endDate &&
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

  getSuppliers(){
    const { data } = this.props;
    const { showAllSuppliers } = this.state;
    const suppliers = data.get('awards', List()).flatMap(award => award.get('suppliers'));
    return showAllSuppliers ?
      suppliers :
      suppliers.slice(0, 2);
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
