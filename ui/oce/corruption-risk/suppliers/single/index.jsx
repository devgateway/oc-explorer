import { Map, Set, List } from 'immutable';
import TopSearch from '../../top-search';
import translatable from '../../../translatable';
import Visualization from '../../../visualization';
import CRDPage from '../../page';
import { wireProps } from '../../tools';
import NrLostVsWon from './donuts/nr-lost-vs-won';
import AmountLostVsWon from './donuts/amount-lost-vs-won';
import NrFlags from './donuts/nr-flags';
import styles from './style.less';
import { cacheFn, pluckImm } from '../../../tools';
import Zoomable from '../../zoomable';
import WinsAndLosses from './bars/wins-and-losses';
import Crosstab from '../../clickable-crosstab';
import { CORRUPTION_TYPES } from '../../constants';
import FlaggedNr from './bars/flagged-nr';
import BackendDateFilterable from '../../backend-date-filterable';

const TitleBelow = ({ title, children, filters, ...props }) => (
  <div>
    {React.cloneElement(
       React.Children.only(children)
    , props)}
    <h4 className="title text-center">
      <button className="btn btn-default btn-sm zoom-button">
        <i className="glyphicon glyphicon-fullscreen" style={{ pointerEvents: 'none' }}/>
      </button>
      &nbsp;
      {title}
    </h4>
  </div>
);

function cutWinsAndLosses(data) {
  if (!data) return data;
  const cutData = JSON.parse(JSON.stringify(data));
  cutData.forEach(datum => {
    datum.x.splice(5);
    datum.y.splice(5);
  });
  return cutData;
}

function cutNrFlags(data) {
  if (!data) return data;
  return data.slice(0, 5);
}

class CrosstabExplanation extends translatable(React.PureComponent) {
  render() {
    const { nrFlags, corruptionType } = this.props;
    return (
      <p>
        {this.t('crd:supplier:crosstabs:explanation')
          .replace('$#$', nrFlags)
          .replace('$#$', this.t(`crd:corruptionType:${corruptionType}:pageTitle`))}
      </p>
    );
  }
}

class Info extends translatable(Visualization) {
  getCustomEP() {
    const { id } = this.props;
    return [
      `ocds/organization/supplier/id/${id}`,
      `totalFlags?supplierId=${id}`,
      `ocds/release/count?supplierId=${id}`,
    ];
  }

  transform([info, _totalFlags, totalContracts]) {
    let totalFlags = 0;
    try {
      totalFlags = _totalFlags[0].flaggedCount;
    } catch(e) {
      console.log('Total flags fetching failed', e);
    }
    return {
      info,
      totalFlags,
      totalContracts,
    };
  }

  render() {
    const { data } = this.props;

    if (!data) return null;

    const info = data.get('info');
    const flagCount = data.get('totalFlags');
    const contractCount = data.get('totalContracts');

    const address = info.get('address');
    const contact = info.get('contactPoint');
    return (
      <section className="info">
        <table className="table table-bordered join-bottom info-table">
          <tbody>
            <tr>
              <td>
                <dl>
                  <dt>{this.t('crd:supplier:ID')}</dt>
                  <dd>{info.get('id')}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>{this.t('crd:supplier:name')}</dt>
                  <dd>{info.get('name')}</dd>
                </dl>
              </td>
              <td className="flags">
                <img src="assets/icons/flag.svg" alt="Flag icon" className="flag-icon" />
                &nbsp;
                {flagCount}
                &nbsp;
                {this.t(flagCount === 1 ?
                  'crd:contracts:baseInfo:flag:sg' :
                  'crd:contracts:baseInfo:flag:pl')}
                <br />
                <small>
                  (
                    {contractCount}
                    &nbsp;
                    {this.t(contractCount === 1 ?
                      'crd:supplier:contract:sg' :
                      'crd:supplier:contract:pl')}
                  )
                </small>
              </td>
            </tr>
          </tbody>
        </table>
        <table className="table table-bordered info-table">
          <tbody>
            <tr>
              <td>
                <dl>
                  <dt>{this.t('crd:supplier:address')}</dt>
                  {address && <dd>
                    {address.get('streetAddress')}<br />
                    {address.get('locality')} /
                    &nbsp;
                    {address.get('postalCode')} /
                    &nbsp;
                    {address.get('countryName')}
                  </dd>}
                </dl>
              </td>
              <td>
                <dl>
                  <dt>{this.t('crd:supplier:contact')}</dt>
                  {contact && <dd>
                    {contact.get('name')}<br />
                    {contact.get('email')}<br />
                    {contact.get('telephone')}
                  </dd>}
                </dl>
              </td>
            </tr>
          </tbody>
        </table>
      </section>
    );
  }
}

class Supplier extends CRDPage {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};

    this.injectSupplierFilter = cacheFn((filters, supplierId) => {
      return filters.update('supplierId', Set(), supplierIds => supplierIds.add(supplierId));
    });

    this.injectBidderFilter = cacheFn((filters, supplierId) => {
      return filters.update('bidderId', Set(), supplierIds => supplierIds.add(supplierId));
    });

    this.groupIndicators = cacheFn((indicatorTypesMapping) => {
      const result = {};
      CORRUPTION_TYPES.forEach((corruptionType) => { result[corruptionType] = []; });
      if (indicatorTypesMapping) {
        Object.keys(indicatorTypesMapping).forEach((indicatorName) => {
          const indicator = indicatorTypesMapping[indicatorName];
          indicator.types.forEach(type => result[type].push(indicatorName));
        });
      }
      return result;
    });
  }

  maybeGetFlagAnalysis() {
    const { indicatorTypesMapping, id, data, filters, translations, requestNewData } = this.props;

    const nrFlagsByCorruptionType = {};
    CORRUPTION_TYPES.forEach((corruptionType) => { nrFlagsByCorruptionType[corruptionType] = 0; });
    data.get('nr-flags', List()).forEach((corruptionType) => {
      nrFlagsByCorruptionType[corruptionType.get('type')] = corruptionType.get('indicatorCount');
    });

    const indicators = this.groupIndicators(indicatorTypesMapping);
    const noIndicators = Object
      .keys(nrFlagsByCorruptionType)
      .every(key => nrFlagsByCorruptionType[key] === 0);

    if (noIndicators) {
      return (
        <section className="flag-analysis">
          <h4>This supplier has no flags</h4>
        </section>
      );
    }

    return (
      <section className="flag-analysis">
        <br />
        {CORRUPTION_TYPES
          .filter(corruptionType => nrFlagsByCorruptionType[corruptionType])
          .map((corruptionType) => {
            return (
              <div>
                <h3>
                  {this.t(`crd:corruptionType:${corruptionType}:pageTitle`)}
                </h3>
                <CrosstabExplanation
                  translations={translations}
                  corruptionType={corruptionType}
                  nrFlags={nrFlagsByCorruptionType[corruptionType]}
                />
                <Crosstab
                  {...wireProps(this, ['crosstab', corruptionType])}
                  filters={this.injectSupplierFilter(filters, id)}
                  requestNewData={(path, newData) => {
                    const toRemove = newData.filter(row => row.every(cell => cell.get('count') === 0)).keySeq();
                    requestNewData(path.concat(['crosstab', corruptionType]),
                      newData.withMutations(data => {
                        toRemove.forEach(indicator => {
                          data.delete(indicator);
                          data.keySeq().forEach(key => {
                            data.deleteIn([key, indicator]);
                          })
                        })
                      })
                    );
                  }}
                  indicators={indicators[corruptionType]}
                  showRawNumbers
                />
              </div>
            );
          })}
      </section>
    );
  }

  render() {
    const { translations, doSearch, id, data } = this.props;
    const totalFlags = data.getIn(['info', 'totalFlags']);

    return (
      <div className="supplier-page">
        <TopSearch
          translations={translations}
          doSearch={doSearch}
          placeholder={this.t('crd:suppliers:top-search')}
        />
        <BackendDateFilterable
          {...wireProps(this, 'info')}
        >
          <Info
            id={id}
          />
        </BackendDateFilterable>

        {totalFlags === 0 && <section className="flag-analysis">
          <h2>{this.t('crd:contracts:flagAnalysis')}</h2>
          <h4>This supplier has no flags</h4>
        </section>}

        {!!totalFlags && this.maybeGetSections()}
      </div>
    );
  }

  maybeGetSections() {
    const { width, id, filters, styling, indicatorTypesMapping } = this.props;
    const donutSize = width / 3 - window.innerWidth / 20;
    const barChartWidth = width / 2 - 100;
    return (
      <div>
        <section className="supplier-general-statistics">
          <h2>{this.t('crd:supplier:generalStatistics')}</h2>
          <div className="col-sm-4">
            <NrLostVsWon
              {...wireProps(this, 'nr-lost-vs-won')}
              filters={this.injectBidderFilter(filters, id)}
              width={donutSize}
            />
          </div>
          <div className="col-sm-4">
            <AmountLostVsWon
              {...wireProps(this, 'amount-lost-vs-won')}
              filters={this.injectBidderFilter(filters, id)}
              width={donutSize}
              styling={styling}
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
          </h2>
          <div className="col-sm-6">
            <Zoomable
              {...wireProps(this, 'wins-and-losses')}
              width={barChartWidth}
              zoomedWidth={width}
              cutData={cutWinsAndLosses}
            >
              <TitleBelow title={this.t('crd:supplier:winsAndLosses:title')}>
                <WinsAndLosses
                  filters={this.injectSupplierFilter(filters, id)}
                />
              </TitleBelow>
            </Zoomable>
          </div>
          <div className="col-sm-6">
            <Zoomable
              {...wireProps(this, 'nr-flagged')}
              width={barChartWidth}
              zoomedWidth={width}
              cutData={cutNrFlags}
            >
              <TitleBelow
                title={this.t('crd:supplier:flaggedNr:title')}
              >
                <FlaggedNr
                  filters={this.injectSupplierFilter(filters, id)}
                  indicatorTypesMapping={indicatorTypesMapping}
                />
              </TitleBelow>
            </Zoomable>
          </div>
        </section>
        {this.maybeGetFlagAnalysis()}
      </div>
    );
  }
}

export default Supplier;
