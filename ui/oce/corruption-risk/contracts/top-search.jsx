import translatable from '../../translatable';

class TopSearch extends translatable(React.Component) {
  componentDidMount(){
    const { searchQuery } = this.props;
    if (searchQuery) this.input.value = searchQuery;
  }

  render() {
    const { doSearch } = this.props;
    return (
      <div className="row">
        <form action="javascript:void(0);" onSubmit={() => doSearch(this.input.value)}>
          <div className="input-group col-sm-4 top-search">
            <input
              type="text"
              className="form-control"
              placeholder={this.t('crd:contracts:top-search')}
              ref={c => this.input = c}
            />
            <div className="input-group-addon">
              <i className="glyphicon glyphicon-search"/>
            </div>
          </div>
        </form>
      </div>
    )
  }
}

export default TopSearch;
