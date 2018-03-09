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
    const { translations, doSearch, id } = this.props;
    return (
      <div className="procuring-entity-page">
        <TopSearch
          translations={translations}
          doSearch={doSearch}
          placeholder={this.t('crd:procuringEntities:top-search')}
        />
        <Info translations={translations} />
      </div>
    );
  }
}

export default ProcuringEntity;
