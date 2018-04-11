import TopSearch from '../../top-search';
import translatable from '../../../translatable';
import Info from './info';
import {
  PEId,
  PEFlaggedNrData,
  winsAndFlagsData,
  procurementsByStatusData,
  procurementsByMethodData,
  PEInfo,
  max2ndRowCommonDataLength
} from './state';
import Zoomable from '../../zoomable';
import TitleBelow from '../../archive/title-below';
import WinsAndFlags from '../../suppliers/single/bars/wins-and-flags';
import FlaggedNr from '../../suppliers/single/bars/flagged-nr';
import ProcurementsByStatus from './general/by-status';
import ProcurementsByMethod from './general/by-method';
import ProcurementsTable from './table';
import style from './style.less';

const NAME = 'ProcuringEntitySingle';

class ProcuringEntity extends translatable(React.Component) {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
  }

  componentWillMount() {
    const { id } = this.props;
    PEId.assign('PESingleComponent', id);
    PEInfo.addListener(NAME, () => {
      PEInfo.getState().then(PEInfo => {
        this.setState({ PEName: PEInfo.name })
      });
    });
  }

  componentDidUpdate() {
    const { id } = this.props;
    PEId.assign('PESingleComponent', id);
  }

  componentWillUnmount() {
    PEInfo.removeListener(NAME);
  }

  render() {
    const { translations, doSearch, width } = this.props;
    const { PEName } = this.state;

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
          <div className="row">
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
          </div>
        </section>
        <section className="flag-analysis">
          <h2>
            {this.t('crd:contracts:flagAnalysis')}
          </h2>
          <div className="row">
            <div className="col-sm-6">
              <Zoomable zoomedWidth={width} data={winsAndFlagsData} length={max2ndRowCommonDataLength}>
                <TitleBelow title={this.t('crd:supplier:winsAndLosses:title')}>
                  <WinsAndFlags translations={translations} />
                </TitleBelow>
              </Zoomable>
            </div>
            <div className="col-sm-6">
              <Zoomable zoomedWidth={width} data={PEFlaggedNrData} length={max2ndRowCommonDataLength}>
                <TitleBelow title={this.t('crd:supplier:flaggedNr:title')}>
                  <FlaggedNr translations={translations} />
                </TitleBelow>
              </Zoomable>
            </div>
          </div>
          <h2>Procurements by {PEName}</h2>
          <ProcurementsTable translations={translations} />
        </section>
      </div>
    );
  }
}

export default ProcuringEntity;
