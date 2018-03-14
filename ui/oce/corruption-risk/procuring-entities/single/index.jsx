import TopSearch from '../../top-search';
import translatable from '../../../translatable';
import Info from './info';
import { PEId, flaggedNrData, winsAndFlagsData } from './state';
import Zoomable from '../../zoomable';
import TitleBelow from '../../archive/title-below';
import WinsAndFlags from '../../suppliers/single/bars/wins-and-flags';
import FlaggedNr from '../../suppliers/single/bars/flagged-nr';

class ProcuringEntity extends translatable(React.Component) {
  componentWillMount() {
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
            <Zoomable zoomedWidth={width} data={flaggedNrData}>
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
