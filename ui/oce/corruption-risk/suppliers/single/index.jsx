import { Map } from 'immutable';
import TopSearch from '../../top-search';
import translatable from '../../../translatable';
import Visualization from '../../../visualization';
import CRDPage from '../../page';
import { wireProps } from '../../tools';
import Donut from '../../donut';
import NrLostVsWon from './donuts/nr-lost-vs-won';
import TwoRowsCenterText from '../../donut/two-rows-center-text';

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
    const { translations, width, doSearch, id } = this.props;
    const donutSize = width / 3 - 100;

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

        <section className="supplier-general-statistics">
          <h2>Supplier General Statistics</h2>
          <div className="col-sm-4">
            <NrLostVsWon
              {...wireProps(this, 'nr-lost-vs-won')}
              width={donutSize}
              data={[1, 2]}
              />
          </div>
          <div className="col-sm-4">
            <Donut
              {...wireProps(this, 'amount-lost-vs-won')}
              CenterText={TwoRowsCenterText}
              width={donutSize}
              title="$ Amount"
              subtitle="Won vs Lost"
              data={[1, 2]}
              values={[{
                  color: '#72c47e',
              }, {
                  color: '#2e833a',
              }]}
            />
          </div>
          <div className="col-sm-4">
            <Donut
              {...wireProps(this, 'flags')}
              CenterText={TwoRowsCenterText}
              width={donutSize}
              title="Total Flags"
              subtitle="by Risk Type"
              data={[1, 2, 3]}
              values={[{
                  color: '#fbc42c',
              }, {
                  color: '#3372b2',
              }, {
                  color: '#30a0f5',
              }]}
            />
          </div>
        </section>
      </div>
    );
  }
}

export default Supplier;
