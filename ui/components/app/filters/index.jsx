import Component from "../../pure-render-component";
import style from "./style.less";
import cn from "classnames";
import MultipleSelect from "./multiple-select";
import TypeAhead from "./type-ahead";
import translatable from "../../translatable";

class Filters extends translatable(Component){
  render(){
    var {actions, state} = this.props;
    var globalState = state.get('globalState');
    var open = 'filters' == globalState.get('filtersBox');
    var filters = globalState.get('filters');
    return (
        <section
            onClick={e => actions.setFiltersBox(open ? "" : "filters")}
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
              <TypeAhead
                  slug="procuringEntities"
                  query={globalState.get('procuringEntityQuery')}
                  state={filters.get('procuringEntities')}
                  actions={actions}
              />
              <section className="buttons">
                <button className="btn btn-primary" onClick={e => actions.applyFilters()}>
                  {this.__('Apply')}
                </button>
                &nbsp;
                <button className="btn btn-default" onClick={e => actions.resetFilters()}>
                  {this.__('Reset')}
                  </button>
              </section>
            </div>
          </div>
        </section>
    )
  }
}

Filters.propTypes = {
  translations: React.PropTypes.object.isRequired
};

export default Filters;
