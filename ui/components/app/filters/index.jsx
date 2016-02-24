import Component from "../../pure-render-component";
import style from "./style.less";
import cn from "classnames";

export default class Filters extends Component{
  render(){
    var {actions, state} = this.props;
    var open = state.getIn(['globalState', 'filtersBoxOpen']);
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
            <div className="box">
              here be box
            </div>
          </div>
        </section>
    )
  }
}