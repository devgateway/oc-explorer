import TopSearch from '../../top-search';
import translatable from '../../../translatable';
import Info from './info';
import { PEId, PEFlaggedNrData, winsAndFlagsData, procurementsByStatusData, procurementsByMethodData } from './state';
import Zoomable from '../../zoomable';
import TitleBelow from '../../archive/title-below';
import WinsAndFlags from '../../suppliers/single/bars/wins-and-flags';
import FlaggedNr from '../../suppliers/single/bars/flagged-nr';
import ProcurementsByStatus from './general/by-status';
import ProcurementsByMethod from './general/by-method';

class ProcuringEntity extends translatable(React.Component) {
  componentWillMount() {
    const { id } = this.props;
    PEId.assign('PESingleComponent', id);
  }

  componentDidUpdate() {
    const { id } = this.props;
    PEId.assign('PESingleComponent', id);
  }

  render() {
    const { translations, doSearch, width } = this.props;
    return (
      <div className="pe-page single-page">
        <TopSearch
          translations={translations}
          doSearch={doSearch}
          placeholder={this.t('crd:procuringEntities:top-search')}
        />
        <Info translations={translations} />
        <section className="pe-general-statistics">
          <h2>{this.t('crd:procuringEntities:generalStatistics')}</h2>
          <div className="col-sm-6">
            <Zoomable zoomedWidth={width} data={procurementsByStatusData}>
              <TitleBelow title={this.t('crd:procuringEntities:byStatus:title')}>
                <ProcurementsByStatus translations={translations} />
              </TitleBelow>
            </Zoomable>
          </div>
          <div className="col-sm-6">
            <Zoomable zoomedWidth={width} data={procurementsByMethodData}>
              <TitleBelow title={this.t('crd:procuringEntities:byMethod:title')}>
                <ProcurementsByMethod translations={translations} />
              </TitleBelow>
            </Zoomable>
          </div>
        </section>
        <br />
        <section className="flag-analysis">
          <h2>
            {this.t('crd:contracts:flagAnalysis')}
          </h2>
          <div className="col-sm-6">
            <Zoomable zoomedWidth={width} data={winsAndFlagsData}>
              <TitleBelow title={this.t('crd:supplier:winsAndLosses:title')}>
                <WinsAndFlags translations={translations} />
              </TitleBelow>
            </Zoomable>
          </div>
          <div className="col-sm-6">
            <Zoomable zoomedWidth={width} data={PEFlaggedNrData}>
              <TitleBelow title={this.t('crd:supplier:flaggedNr:title')}>
                <FlaggedNr translations={translations} />
              </TitleBelow>
            </Zoomable>
          </div>
        </section>
      </div>
    );
  }
}

export default ProcuringEntity;
