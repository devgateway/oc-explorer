import Component from "../../../pure-render-component";
import cn from "classnames";
import style from "./style.less";

var pluralize = (sg, pl) => count => count == 1 ? `${count} ${sg}` : `${count} ${pl}`;
var pluralizeResults = pluralize("result", "results");

export default class TypeAhead extends Component{
  render(){
    var {query, actions, state, slug} = this.props;
    if(!state) return null;
    var options = state.get('options');
    var selectedCount = options.filter(option => option.get('selected')).count();
    var totalOptions = options.count();
    var open = state.get('open');
    var haveQuery = query.length >= 3;
    return (
        <section className={cn('field procuring-entities', {open: open})}>
          <header onClick={e => actions.toggleFilter(slug, !open)}>
            <i className="glyphicon glyphicon-menu-right"></i> Procuring entity
            <span className="pull-right count">({selectedCount}/{totalOptions})</span>
          </header>
          <section className="options">
            <input
                type="text"
                value={query}
                onChange={e => actions.updateProcuringEntityQuery(e.target.value)}
            />
          </section>
          {haveQuery ?
            <div>{pluralizeResults(totalOptions)}</div>
          : null}
          {options.map(option => (
              <div className="checkbox" key={option.get('id')}>
                <label>
                  <input
                      type="checkbox"
                      value={option.get('selected')}
                      onChange={e => actions.toggleFilterOption(slug, option.get('id'), !option.get('selected'))}
                  /> {option.get('name')}
                </label>
              </div>
          )).toArray()}
        </section>
    )
  }
}