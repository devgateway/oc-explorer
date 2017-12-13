import translatable from '../../translatable';

class TopSearch extends translatable(React.Component) {
  constructor(props, ...rest){
    super(props, ...rest);
    this.state = {
      inputValue: props.searchQuery
    }
  }

  render() {
    const { doSearch } = this.props;
    const { inputValue } = this.state;
    return (
      <div className="row">
        <form action="javascript:void(0);" onSubmit={() => doSearch(inputValue)}>
          <div className="input-group col-sm-4 top-search">
            <input
              type="text"
              className="form-control"
              placeholder={this.t('crd:contracts:top-search')}
              value={inputValue}
              onChange={e => this.setState({ inputValue: e.target.value })}
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
