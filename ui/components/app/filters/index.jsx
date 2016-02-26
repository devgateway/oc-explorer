import Component from "../../pure-render-component";
import style from "./style.less";
import cn from "classnames";
import MultipleSelect from "./multiple-select";

export default class Filters extends Component{
  render(){
    var {actions, state} = this.props;
    var globalState = state.get('globalState');
    var open = globalState.get('filtersBoxOpen');
    var filters = globalState.get('filters');
    return (
        <section
            onClick={e => actions.toggleFiltersBox(!open)}
            className={cn("col-sm-12 filters", {open: open})}
        >
          <div className="row">
            <div className="col-sm-10 text">
              Filter the data
            </div>
            <div className="col-sm-1 end arrow">
              <i className="glyphicon glyphicon-menu-right"></i>
            </div>
            <div className="box" onClick={e => e.stopPropagation()}>
              <MultipleSelect title="Bid type" slug="bidTypes" state={filters.get('bidTypes')} actions={actions}/>
              <MultipleSelect
                  title="Bid selection method"
                  slug="bidSelectionMethods"
                  state={filters.get('bidSelectionMethods')}
                  actions={actions}
              />
            </div>
          </div>
        </section>
    )
  }
}