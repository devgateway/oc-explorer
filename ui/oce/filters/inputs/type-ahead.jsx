import translatable from "../../translatable";
import Component from "../../pure-render-component";
import {fetchJson} from "../../tools";
import URI from "urijs";
import {fromJS} from "immutable";

const MIN_QUERY_LENGTH = 3;

class TypeAhead extends translatable(Component){
  constructor(props){
    super(props);
    this.state = {
      query: "",
      options: fromJS([])
    }
  }

  updateQuery(query){
    this.setState({query});
    if (query.length >= MIN_QUERY_LENGTH) {
      fetchJson(new URI('/api/ocds/organization/procuringEntity/all').addSearch('text', query).toString())
          .then(data => this.setState({options: fromJS(data)}));
    }
  }

  render(){
    let {query, options} = this.state;
    let {selected, onToggle} = this.props;
    let haveQuery = query.length >= MIN_QUERY_LENGTH;
    return (
        <section className="field type-ahead">
          <header>{this.__('Procuring Entity')} ({selected.count()})</header>
          <section className="options">
            <input
                type="text"
                className="input-sm form-control search"
                placeholder={this.__("type search query")}
                value={query}
                onChange={e => this.updateQuery(e.target.value)}
            />

            {haveQuery && <div className="result-count">{this.__n("result", "results", options.count())}</div>}

            {options.map(option => (
                <div className="checkbox" >
                  <label key={option.get('id')}>
                    <input
                        key={option.get('id')}
                        type="checkbox"
                        checked={selected.has(option.get('id'))}
                        onChange={e => onToggle(option.get('id'))}
                    /> {option.get('name')}
                  </label>
                </div>
            )).toArray()}
          </section>
        </section>
    )
  }
}

export default TypeAhead;