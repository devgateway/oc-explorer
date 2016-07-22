import translatable from "../../translatable";
import Component from "../../pure-render-component";
import {fromJS} from "immutable";

class MultipleSelect extends translatable(Component){
  constructor(props){
    super(props);
  }

  getOptions(){
    return fromJS([]);
  }

  getSelectedCount(){
    return this.getOptions().filter((_, id) => this.props.selected.has(id)).count();
  }

  render(){
    let options = this.getOptions();
    let {selected, onToggle} = this.props;
    let totalOptions = options.count();
    return (
        <section className="field">
          <header>
            {this.getTitle()} <span className="count">({this.getSelectedCount()}/{totalOptions})</span>
          </header>
          <section className="options">
            {options.map((option, key) => (
                <div className="checkbox" key={this.getId(option, key)}>
                  <label>
                    <input
                        type="checkbox"
                        checked={selected.has(this.getId(option, key))}
                        onChange={e => onToggle(this.getId(option, key))}
                    /> {this.getLabel(option)}
                  </label>
                </div>
            )).toArray()}
          </section>
        </section>
    )
  }
}

export default MultipleSelect;