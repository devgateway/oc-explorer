import frontendYearFilterable from "../../frontend-year-filterable";
import { Map, TileLayer } from 'react-leaflet';
import {pluck} from "../../tools";
import Cluster from "./cluster";
import Location from "./location";
import Visualization from "../../visualization";
import style from "./style.less";

class MapVisual extends frontendYearFilterable(Visualization){
  getMaxAmount(){
    return Math.max(0, ...this.getData().map(pluck('amount')));
  }

  render(){
    return <Map center={[14.5, 105]} zoom={5}>
      <TileLayer
          url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'
          attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
      />
      <Cluster maxAmount={this.getMaxAmount()}>
        {this.getData().map(location => (
            <this.constructor.Location
                key={location._id}
                position={location.coords.reverse()}
                maxAmount={this.getMaxAmount()}
                data={location}
            />
        ))}
      </Cluster>
    </Map>
  }
}

MapVisual.propTypes = {};
MapVisual.computeComparisonYears = null;
MapVisual.Location = Location;

export default MapVisual;
