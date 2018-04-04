import translatable from '../../translatable';
// eslint-disable-next-line no-unused-vars
import style from './style.less';

const isExactMatch = a => a.indexOf('"') === 0 &&
      a.lastIndexOf('"') === a.length - 1 &&
      a.length > 1;

class TopSearch extends translatable(React.Component) {
  constructor(props, ...rest) {
    super(props, ...rest);
    this.state = {
      inputValue: props.searchQuery || '',
    };
  }

  toggleExactMatch() {
    const { inputValue } = this.state;
    const exactMatch = isExactMatch(inputValue);
    const newValue = exactMatch ?
      inputValue.slice(1, -1) :
      `"${inputValue}"`;

    this.setState(
      { inputValue: newValue },
      this.props.doSearch.bind(null, newValue),
    );
  }

  render() {
    const { doSearch, placeholder } = this.props;
    const { inputValue } = this.state;
    const exactMatch = isExactMatch(inputValue);

    return (
        <form
          action="javascript:void(0);"
          className="top-search row"
          onSubmit={() => doSearch(inputValue)}
        >
          <div className="form-group col-sm-6">
            <div className="input-group">
              <input
                type="text"
                className="form-control"
                placeholder={placeholder}
                value={inputValue}
                onChange={e => this.setState({ inputValue: e.target.value })}
              />
              <div className="input-group-addon">
                <i className="glyphicon glyphicon-search" />
              </div>
            </div>
          </div>
          <div className="form-group exact-match col-sm-6">
            <input
              id="exactMatch"
              type="checkbox"
              checked={exactMatch}
              onChange={() => this.toggleExactMatch()}
            />
            &nbsp;
            <label htmlFor="exactMatch">
              {this.t('crd:contracts:hint')}
            </label>
          </div>
        </form>
    );
  }
}

export default TopSearch;
