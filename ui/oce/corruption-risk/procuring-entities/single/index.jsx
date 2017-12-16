import TopSearch from '../../top-search';
import translatable from '../../../translatable';

class ProcuringEntity extends translatable(React.Component) {
  render() {
    const { translations, doSearch } = this.props;
    return (
      <div className="procuring-entity-page">
        <TopSearch
          translations={translations}
          doSearch={doSearch}
          placeholder={this.t('crd:procuringEntities:top-search')}
        />
      </div>
    );
  }
}

export default ProcuringEntity;
