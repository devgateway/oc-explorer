import TopSearch from '../../top-search';
import translatable from '../../../translatable';

class Supplier extends translatable(React.Component) {
  render() {
    const { translations, doSearch } = this.props;
    return (
      <div className="supplier-page">
        <TopSearch
          translations={translations}
          doSearch={doSearch}
          placeholder={this.t('crd:suppliers:top-search')}
        />
      </div>
    );
  }
}

export default Supplier;
