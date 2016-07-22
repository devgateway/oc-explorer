import Tab from "../../../oce/filters/tabs";
import BidTypes from "../bid-types";
import {Set} from "immutable";

class ProcurementTypes extends Tab{
  render(){
    let {state, onUpdate, bidTypes} = this.props;
    let selectedBidTypesIds = state.get('bidTypeId', Set());
    return <div>
      <BidTypes
          options={bidTypes}
          selected={selectedBidTypesIds}
          onToggle={id => onUpdate('bidTypeId', selectedBidTypesIds.has(id) ?
              selectedBidTypesIds.delete(id) :
              selectedBidTypesIds.add(id))
          }
      />
    </div>
  }
}

ProcurementTypes.getName = __ => __('Procurement types');

export default ProcurementTypes;