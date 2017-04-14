import cn from "classnames";
import {Set, Map} from "immutable";
import Organizations from "./organizations";
import ProcurementMethodBox from "./procurement-method";
import ValueAmount from "./value-amount";

class Filters extends React.Component{
  render(){
    const {state, onUpdate, translations, currentBoxIndex, requestNewBox} = this.props;
    const {BOXES} = this.constructor;
    return (
      <div className="row filters-bar" onClick={e => e.stopPropagation()}>
        <div className="col-lg-3 col-md-2 col-sm-1 title text-right">
          Filter your data
        </div>
        <div className="col-lg-7 col-md-9 col-sm-10">
          {BOXES.map((Box, index) => {
             return (
               <Box
                   key={index}
                   open={currentBoxIndex === index}
                   onClick={e => requestNewBox(currentBoxIndex === index ? null : index)}
                   state={state}
                   onUpdate={(slug, newState) => onUpdate(state.set(slug, newState))}
                   translations={translations}
               />
             )
           })}
        </div>
        <div className="col-lg-2 col-md-1 col-sm-1 download">
          <button className="btn btn-success">
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
  ValueAmount
];

class OldFilters extends React.Component{
  render(){
    const {box, requestNewFilterBox, onUpdate, state} = this.props;
    const setBox = newBox => e => {
      e.stopPropagation();
      if(box == newBox){
        requestNewFilterBox('');
      } else {
        requestNewFilterBox(newBox);
      }
    };

    const filters = [{
      title: "Organizations",
      slug:  "organizations",
      Component: Organizations
    },{
      title: "Procurement method",
      slug:  "procurementMethod",
      Component: ProcurementMethod
    }];
    /* ,{
       title: "Date",
       slug:  "date"
       },{
       title: "Location",
       slug:  "location"
       }];*/
    const minTenderPrice = state.get('minTenderPrice');
    const maxTenderPrice = state.get('maxTenderPrice');
    const minAwardValue = state.get('minAwardValue');
    const maxAwardValue = state.get('maxAwardValue');
    return (
      <div className="row filters-bar">
        <div className="col-lg-3 col-md-2 col-sm-1 title text-right">
          Filter your data
        </div>
        <div className="col-lg-7 col-md-9 col-sm-10">
	        {filters.map(({title, slug, Component}, index) => {
             const selected = state.get(slug, Set());
             const toggle = id => onUpdate(state.set(slug, selected.has(id) ?
                                                     selected.delete(id) :
                                                     selected.add(id)));
             return (
  	           <Filter
    	    	       title={title}
	        	       open={box == slug}
		               onClick={setBox(slug)}
  	               key={index}
    	         >
                 <Component
                     selected={selected}
                     translations={Map()}
                     onToggle={toggle}
                 />
               </Filter>
             )
           })}
        <Filter
            title="Value amount"
            open={box == 'valueAmount'}
            onClick={setBox('valueAmount')}
            key={'valueAmount'}
        >
          <TenderPrice
              translations={Map()}
              minValue={minTenderPrice}
              maxValue={maxTenderPrice}
              onUpdate={({min, max}) => {
                  minTenderPrice != min && onUpdate(state.set("minTenderValue", min));
                  maxTenderPrice != max && onUpdate(state.set("maxTenderValue", max));
                }}
          />
          <AwardValue
              translations={Map()}
              minValue={minAwardValue}
              maxValue={maxAwardValue}
              onUpdate={({min, max}) => {
                  minAwardValue != min && onUpdate(state.set("minAwardValue", min));
                  maxAwardValue != max && onUpdate(state.set("maxAwardValue", max));
                }}
      />
          </Filter>
      </div>
      <div className="col-lg-2 col-md-1 col-sm-1 download">
        <button className="btn btn-success">
          <i className="glyphicon glyphicon-download-alt"></i>
        </button>
      </div>
      </div>
    )
  }
}

export default Filters;
