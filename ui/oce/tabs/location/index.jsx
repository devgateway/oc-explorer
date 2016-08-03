import Tab from "../index";
import frontendYearFilterable from "../../frontend-year-filterable";
import { Map, TileLayer } from 'react-leaflet';
import {pluck} from "../../tools";
import Cluster from "./cluster";
import Location from "./location";

class LocationTab extends frontendYearFilterable(Tab){
  getData(){
    return super.getData()
        .groupBy(location => location.getIn(['budget.projectLocation', '_id']))
        .map(locations => locations.reduce((reducedLocation, location) => {
          return {
            "_id": location.getIn(['budget.projectLocation', '_id']),
            "name": location.getIn(['budget.projectLocation', 'description']),
            "amount": reducedLocation.amount + location.get('totalPlannedAmount'),
            "count": reducedLocation.count + location.get('recordsCount'),
            "coords": location.getIn(['budget.projectLocation', 'geometry', 'coordinates']).toJS()
          }
        }, {
          "amount": 0,
          "count": 0
        }))
        .toArray()
  }

  getMaxAmount(){
    return Math.max(0, ...this.getData().map(pluck('amount')));
  }

  static getName(__){
    return __("Location")
  }

  render(){
    return <div className="col-sm-12 content map-content">
      <Map center={[14.5, 105]} zoom={5}>
        <TileLayer
            url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'
            attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        />
        <Cluster maxAmount={this.getMaxAmount()}>
          {this.getData().map(location => (
              <Location
                  key={location._id}
                  position={location.coords.reverse()}
                  maxAmount={this.getMaxAmount()}
                  data={location}
              />
          ))}
        </Cluster>
      </Map>
    </div>
  }
}

LocationTab.icon = "planning";
LocationTab.computeComparisonYears = null;

export default LocationTab;
