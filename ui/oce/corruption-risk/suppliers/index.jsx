import TopSearch from '../top-search';
import translatable from '../../translatable';

class Suppliers extends translatable(React.Component) {
  render() {
    const { translations, searchQuery, doSearch } = this.props;
    return (
      <div className="suppliers-page">
        <TopSearch
          translations={translations}
          searchQuery={searchQuery}
          doSearch={doSearch}
          placeholder={this.t('crd:suppliers:top-search')}
        />
      </div>
    );
  }
}

export default Suppliers;
