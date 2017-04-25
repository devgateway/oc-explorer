import FilterTab from "../../filters/tabs";
import cn from "classnames";

class FilterBox extends FilterTab{
  isActive(){
    console.warn(`Implement an "isActive" method for ${this.getTitle()}`);
  }

  render(){
    const {open, onClick, onApply, state} = this.props;
    return(
      <div onClick={onClick} className={cn('filter', {open, active: this.isActive()})}>
        <span className="box-title">
          {this.getTitle()}
        </span>
        <i className="glyphicon glyphicon-menu-down"></i>
        {open && <div className="dropdown" onClick={e => e.stopPropagation()}>
        {this.getBox()}
          <div className="controls">
            <button className="btn btn-primary" onClick={onApply}>Apply</button>
          </div>
        </div>}
      </div>
    )
  }
}

export default FilterBox;
