import TenderLocations from "../../../oce/visualizations/map/tender-locations";
import TenderLocation, {ChartTab} from "../../../oce/visualizations/map/tender-locations/location";
import BidSelectionMethod from "../../visualizations/bid-selection-method";

class BidSelectionMethodTab extends ChartTab{
  static getName(__){
    return __('Procurement method');
  }
}

BidSelectionMethodTab.Chart = BidSelectionMethod;

class OCVNTenderLocation extends TenderLocation{}

OCVNTenderLocation.TABS = TenderLocation.TABS.concat(BidSelectionMethodTab);

class OCVNTenderLocations extends TenderLocations{}

OCVNTenderLocations.Location = OCVNTenderLocation;

export default OCVNTenderLocations;