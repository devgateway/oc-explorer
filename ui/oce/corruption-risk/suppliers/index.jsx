import TopSearch from '../top-search';

class Suppliers extends React.Component {
  render() {
    const { translations, searchQuery, doSearch } = this.props;
    return (
      <div className="suppliers-page">
        <TopSearch
          translations={translations}
          searchQuery={searchQuery}
          doSearch={doSearch}
        />
      </div>
    );
  }
}

export default Suppliers;
