import Component from "../../pure-render-component";
import cn from "classnames";
import translatable from "../../translatable";

let icon = slug => <i className={`glyphicon glyphicon-${slug}`}></i>

export default class ComparisionCriteria extends translatable(Component){
  render(){
    let {onClick, onChange, open, compareBy} = this.props;
    return <div
        onClick={onClick}
        className={cn("filters compare", {open})}
    >
      {icon('th-large')} {this.__('Compare')} {icon('menu-down')}
      <div className="box" onClick={e => e.stopPropagation()}>
        <div className="col-sm-6">
          <label>{this.__('Comparison criteria')}</label>
        </div>
        <div className="col-sm-6">
          <select
              className="form-control"
              value={compareBy}
              onChange={e => onChange(e.target.value)}
          >
            <option value="">{this.__('None')}</option>
            <option value="bidTypeId">{this.__('Bid Type')}</option>
            <option value="bidSelectionMethod">{this.__('Bid Selection Method')}</option>
            <option value="procuringEntityId">{this.__('Procuring Entity')}</option>
          </select>
        </div>
      </div>
    </div>;
  }
}