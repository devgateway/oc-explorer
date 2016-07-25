import Tab from "../../../oce/filters/tabs";
import Locations from '../locations';
import {Set} from "immutable";

class LocationsTab extends Tab{
  render(){
    let {state, onUpdate, translations} = this.props;
    let selectedLocations = state.get('tenderDeliveryLocationIdentifier', Set());
    return <div>
      <Locations
          selected={selectedLocations}
          onToggle={id => onUpdate('tenderDeliveryLocationIdentifier', selectedLocations.has(id) ?
              selectedLocations.delete(id) :
              selectedLocations.add(id))
          }
          translations={translations}
      />
    </div>
  }
}

LocationsTab.getName = __ => __('Locations');

export default LocationsTab;