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
    const hasSpecialChars = inputValue.indexOf('-') > -1;
    const exactMatch = isExactMatch(inputValue);

    return (
      <div className="top-search">
        <div className="row">
          <form action="javascript:void(0);" onSubmit={() => doSearch(inputValue)}>
            <div className="input-group col-lg-4 col-md-6 col-sm-8 top-search">
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
          </form>
        </div>
        <div className="row">
          <div className="col-sm-12">
            <input
              id="exactMatch"
              type="checkbox"
              checked={exactMatch}
              onChange={() => this.toggleExactMatch()}
            />
            &nbsp;
            <label htmlFor="exactMatch">
              Click box to search OCIDs
            </label>
          </div>
        </div>
      </div>
    );
  }
}

export default TopSearch;
