import Location from "../../oce/tabs/location";
import PlannedLocations from '../visualizations/map/planned-locations';
import TenderLocations from "../../oce/visualizations/map/tender-locations";

class OCVNLocation extends Location{}

OCVNLocation.LAYERS = [PlannedLocations, TenderLocations];

export default OCVNLocation;