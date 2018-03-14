import TopSearch from '../../top-search';
import translatable from '../../../translatable';
import Info from './info';
import { PEId } from './state';

class ProcuringEntity extends translatable(React.Component) {
  componentWillMount() {
    const { id } = this.props;
    PEId.assign('PESingleComponent', id);
  }

  render() {
    const { translations, doSearch } = this.props;
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
        </section>
      </div>
    );
  }
}

export default ProcuringEntity;
