import Location from "../../oce/tabs/location";
import PlannedLocations from '../visualizations/map/planned-locations';
import InvitationToBid from "../visualizations/map/invitation-to-bid";

class OCVNLocation extends Location{}

OCVNLocation.LAYERS = [PlannedLocations, InvitationToBid];

export default OCVNLocation;