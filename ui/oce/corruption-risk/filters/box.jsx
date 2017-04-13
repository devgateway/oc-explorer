import FilterTab from "../../filters/tabs";
import cn from "classnames";

class FilterBox extends FilterTab{
  render(){
    const {open, onClick} = this.props;
    return(
      <div onClick={onClick} className={cn('filter', {open})}>
        {this.getTitle()}
      <i className="glyphicon glyphicon-menu-down"></i>
      {open && <div className="dropdown" onClick={e => e.stopPropagation()}>
        {this.getBox()}
      </div>}
      </div>
    )
  }
}

export default FilterBox;
