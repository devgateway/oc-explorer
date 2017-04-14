import FilterTab from "../../filters/tabs";
import cn from "classnames";

class FilterBox extends FilterTab{
  render(){
    const {open, onClick} = this.props;
    return(
      <div onClick={onClick} className={cn('filter', {open})}>
        <span className="box-title">
          {this.getTitle()}
        </span>
        <i className="glyphicon glyphicon-menu-down"></i>
        {open && <div className="dropdown" onClick={e => e.stopPropagation()}>
        {this.getBox()}
          <div className="controls">
            <button className="btn btn-primary">Apply</button>
          </div>
        </div>}
      </div>
    )
  }
}

export default FilterBox;
