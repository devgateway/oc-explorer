import Component from "../../../pure-render-component";
import style from "./style.less";
import cn from "classnames";

export default class MultipleSelect extends Component{
  render(){
    let {title, state, slug, actions} = this.props;
    if(!state) return null;
    let options = state.get('options');
    let selectedCount = options.filter(option => option.get('selected')).count();
    let totalOptions = options.count();
    let open = state.get('open');
    return (
        <section className={cn('col-sm-4', 'field', {open: open})}>
          <header onClick={e => actions.toggleFilter(slug, !open)}>
            {title} <span className="count">({selectedCount}/{totalOptions})</span>
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