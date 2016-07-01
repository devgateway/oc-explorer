import Component from "../../../pure-render-component";
import translatable from "../../../translatable";
import style from "./style.less";

export default class TypeAhead extends translatable(Component){
  render(){
    let {query, actions, state, slug} = this.props;
    if(!state) return null;
    let options = state.get('options');
    let selectedCount = options.filter(option => option.get('selected')).count();
    let totalOptions = options.count();
    let haveQuery = query.length >= 3;
    return (
        <section className="col-sm-4 field procuring-entities">
          <header>
            {this.__('Procuring Entity')} <span className="pull-right count">({selectedCount}/{totalOptions})</span>
          </header>
          <section className="options">
            <input
                type="text"
                className="input-sm form-control search"
                placeholder={this.__("type search query")}
                value={query}
                onChange={e => actions.updateProcuringEntityQuery(e.target.value)}
            />
            {haveQuery ?
                <div className="result-count">{this.__n("result", "results", totalOptions)}</div>
                : null}
            {options.map(option => (
                <div className="checkbox" >
                  <label key={option.get('id')}>
                    <input
                        type="checkbox"
                        value={option.get('selected')}
                        onChange={e => actions.toggleFilterOption(slug, option.get('id'), !option.get('selected'))}
                    /> {option.get('name')}
                  </label>
                </div>
            )).toArray()}
          </section>
        </section>
    )
  }
}