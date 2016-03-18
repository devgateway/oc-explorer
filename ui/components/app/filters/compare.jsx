import Component from "../../pure-render-component";
import cn from "classnames";

export default class ComparisionCriteria extends Component{
  render(){
    var {actions, state} = this.props;
    var globalState = state.get('globalState');
    var open = 'compare' == globalState.get('filtersBox');
    return (
        <section
            onClick={e => actions.setFiltersBox(open ? "" : "compare")}
            className={cn("col-sm-12 filters", {open: open})}
        >
          <div className="row">
            <div className="col-sm-10 text">
              Compare
            </div>
            <div className="col-sm-1 end arrow">
              <i className="glyphicon glyphicon-menu-right"></i>
            </div>
            <div className="box" onClick={e => e.stopPropagation()}>
              <label>
                Comparison criteria
                &nbsp;
                <select class="form-control">
                  <option value="">None</option>
                  <option>Bid Type</option>
                  <option>Bid Selection Method</option>
                  <option>Procuring Entity</option>
                </select>
              </label>
            </div>
          </div>
        </section>
    )
  }
}