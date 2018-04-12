import cn from "classnames";
import {Set, Map} from "immutable";
import Organizations from "./organizations";
import ProcurementMethodBox from "./procurement-method";
import ValueAmount from "./value-amount";
import DateBox from "./date";
import {fetchJson, range, pluck} from "../../tools";
import translatable from '../../translatable';

class Filters extends translatable(React.Component) {
  render(){
    const {onUpdate, translations, currentBoxIndex, requestNewBox, state, allYears, allMonths, onApply, appliedFilters} = this.props;
    const {BOXES} = this.constructor;
    return (
      <div className="row filters-bar" onMouseDown={e => e.stopPropagation()}>
        <div className="col-md-10">
          <div className="title">{this.t('filters:hint')}</div>
          {BOXES.map((Box, index) => {
             return (
               <Box
                 key={index}
                 open={currentBoxIndex === index}
                 onClick={e => requestNewBox(currentBoxIndex === index ? null : index)}
                 state={state}
                 onUpdate={(slug, newState) => onUpdate(state.set(slug, newState))}
                 translations={translations}
                 onApply={newState => onApply(newState)}
                 allYears={allYears}
                 allMonths={allMonths}
                 appliedFilters={appliedFilters}
               />
             )
          })}
        </div>
        <div className="col-md-2 download">
          <button className="btn btn-default" disabled>
            <i className="glyphicon glyphicon-download-alt"></i>
          </button>
        </div>
      </div>
    )
  }
}

Filters.BOXES = [
  Organizations,
  ProcurementMethodBox,
  ValueAmount,
  DateBox
];

export default Filters;
