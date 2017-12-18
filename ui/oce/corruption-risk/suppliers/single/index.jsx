import { Map } from 'immutable';
import TopSearch from '../../top-search';
import translatable from '../../../translatable';
import Visualization from '../../../visualization';
import CRDPage from '../../page';
import { wireProps } from '../../tools';

class Info extends translatable(Visualization) {
  getCustomEP() {
    const { id } = this.props;
    return `ocds/organization/supplier/id/${id}`;
  }

  render() {
    const { data } = this.props;
    if(!data) return null;

    const address = data.get('address');
    const contact = data.get('contactPoint');
    const flagCount = -1;
    return (
      <section className="info">
        <table className="table table-bordered join-bottom info-table">
          <tbody>
            <tr>
              <td>
                <dl>
                  <dt>Supplier ID</dt>
                  <dd>{data.get('id')}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>Supplier Name</dt>
                  <dd>{data.get('name')}</dd>
                </dl>
              </td>
              <td className="flags">
                <img src="assets/icons/flag.svg" alt="Flag icon" className="flag-icon" />
                &nbsp;
                {flagCount}
                &nbsp;{flagCount === 1 ? 'Flag' : 'Flags'}
              </td>
            </tr>
          </tbody>
        </table>
        <table className="table table-bordered info-table">
          <tbody>
            <tr>
              <td>
                <dl>
                  <dt>Supplier address</dt>
                  <dd>
                    {address.get('streetAddress')}<br />
                    {address.get('locality')} /
                    &nbsp;
                    {address.get('postalCode')} /
                    &nbsp;
                    {address.get('countryName')}
                  </dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>Supplier Contact Information</dt>
                  <dd>
                    {contact.get('name')}<br/>
                    {contact.get('email')}<br/>
                    {contact.get('telephone')}
                  </dd>
                </dl>
              </td>
            </tr>
          </tbody>
        </table>
      </section>
    )
  }
}

class Supplier extends CRDPage {
  render() {
    const { translations, doSearch, id } = this.props;
    return (
      <div className="supplier-page">
        <TopSearch
          translations={translations}
          doSearch={doSearch}
          placeholder={this.t('crd:suppliers:top-search')}
        />
        <Info
          {...wireProps(this, 'info')}
          id={id}
          filters={Map()}
        />
      </div>
    );
  }
}

export default Supplier;
