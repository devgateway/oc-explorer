import { Map, Set } from 'immutable';
import { List } from 'immutable';
import TopSearch from '../../top-search';
import translatable from '../../../translatable';
import Visualization from '../../../visualization';
import CRDPage from '../../page';
import { wireProps } from '../../tools';
import Donut from '../../donut';
import NrLostVsWon from './donuts/nr-lost-vs-won';
import AmountLostVsWon from './donuts/amount-lost-vs-won';
import NrFlags from './donuts/nr-flags';
import styles from './style.less';
import { cacheFn, pluckImm } from '../../../tools';
import PlotlyChart from '../../plotly-chart';
import TaggedBarChart from '../../tagged-bar-chart';
import Zoomable from '../../zoomable';

const TitleBelow = ({ title, children, ...props }) => (
  <div>
    {React.cloneElement(
       React.Children.only(children)
    , props)}
    <h4 className="title text-center">
      {title}
    </h4>
  </div>
);

const add = (a, b) => a + b;

class Info extends translatable(Visualization) {
  getCustomEP() {
    const { id } = this.props;
    return `ocds/organization/supplier/id/${id}`;
  }

  render() {
    const { data, flagCount: _flagCount } = this.props;
    const flagCount = (_flagCount || List())
      .map(pluckImm('indicatorCount')).reduce(add, 0);

    if(!data) return null;

    const address = data.get('address');
    const contact = data.get('contactPoint');
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
  constructor(...args) {
    super(...args);
    this.injectSupplierFilter = cacheFn((filters, supplierId) => {
      return filters.update('supplierId', Set(), supplierIds => supplierIds.add(supplierId));
    });
  }

  render() {
    const { translations, width, doSearch, id, filters, data } = this.props;
    const donutSize = width / 3 - 100;
    const barChartWidth = width / 2 - 100;

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
          flagCount={data.get('nr-flags')}
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
            <AmountLostVsWon
              {...wireProps(this, 'amount-lost-vs-won')}
              width={donutSize}
            />
          </div>
          <div className="col-sm-4">
            <NrFlags
              {...wireProps(this, 'nr-flags')}
              filters={this.injectSupplierFilter(filters, id)}
              width={donutSize}
            />
          </div>
        </section>
        <section className="flag-analysis">
          <h2>
            {this.t('crd:contracts:flagAnalysis')}
            &nbsp;
            <small>({this.t('crd:contracts:clickCrosstabHint')})</small>
          </h2>
          <div className="col-sm-6">
            <Zoomable>
              <TitleBelow title="Wins & Flags by Procuring Entity">
                <PlotlyChart
                  data={[{
                      x: ['Supplier 1', 'Supplier 2', 'Supplier 3', 'Supplier 4', 'Supplier 5'],
                      y: [1, 2, 3, 4, 5],
                      name: 'Wins',
                      type: 'bar',
                  }, {
                      x: ['Supplier 1', 'Supplier 2', 'Supplier 3', 'Supplier 4', 'Supplier 5'],
                      y: [5, 4, 3, 2, 1],
                      name: 'Flags',
                      type: 'bar',
                  }]}
                  layout={{
                    width: barChartWidth,
                    height: 250,
                    barmode: 'group',
                    margin: {t: 0, r: 0, b: 30, l: 20, pad: 0},
                    legend: {
                      xanchor: 'right',
                      yanchor: 'top',
                      x: .9,
                      y: 1.5,
                      orientation: 'h',
                    }
                  }}
                />
              </TitleBelow>
            </Zoomable>
          </div>
          <div className="col-sm-6">
            <Zoomable
              width={barChartWidth}
              zoomedWidth={width}
            >
              <TitleBelow title="No. Times Each Indicator is Flagged in Procurements Won by Supplier">
                <TaggedBarChart
                  tags={{
                    FRAUD: {
                      name: 'Fraud',
                      color: '#299df4',
                    },
                    RIGGING: {
                      name: 'Process rigging',
                      color: '#3372b2',
                    },
                    COLLUSION: {
                      name: 'Collusion',
                      color: '#fbc42c',
                    },
                  }}
                  data={[{
                      x: 'Indicator 1',
                      y: 5,
                      tags: ['RIGGING'],
                  }, {
                      x: 'Indicator 2',
                      y: 4,
                      tags: ['COLLUSION', 'FRAUD'],
                  }, {
                      x: 'Indicator 3',
                      y: 3,
                      tags: ['COLLUSION', 'RIGGING'],
                  }, {
                      x: 'Indicator 4',
                      y: 2,
                      tags: ['FRAUD', 'RIGGING'],
                  }, {
                      x: 'Indicator 5',
                      y: 1,
                      tags: ['COLLUSION', 'FRAUD', 'RIGGING'],
                  }]}
                />
              </TitleBelow>
            </Zoomable>
          </div>
        </section>
      </div>
    );
  }
}

export default Supplier;
