import Component from "../../../pure-render-component";
import style from "./style.less";
import cn from "classnames";

export default class MultipleSelect extends Component{
  render(){
    var {title, state, slug, actions} = this.props;
    if(!state) return null;
    var options = state.get('options');
    var selectedCount = options.filter(option => option.get('selected')).count();
    var totalOptions = options.count();
    var open = state.get('open');
    return (
        <section className={cn('field', {open: open})}>
          <header onClick={e => actions.toggleFilter(slug, !open)}>
            <i className="glyphicon glyphicon-menu-right"></i> {title}
            <span className="pull-right count">({selectedCount}/{totalOptions})</span>
          </header>
          <section className="options">
            {options.map(option => (
                <div className="checkbox" key={option.get('id')}>
                  <label>
                    <input
                        type="checkbox"
                        checked={option.get('selected')}
                        onChange={e => actions.toggleFilterOption(slug, option.get('id'), !option.get('selected'))}
                    /> {option.get('description')}
                  </label>
                </div>
            )).toArray()}
          </section>
        </section>
    )
  }
}