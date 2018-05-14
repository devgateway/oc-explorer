import { Map, TileLayer } from 'react-leaflet';
import frontendDateFilterable from '../frontend-date-filterable';
import { pluck } from '../../tools';
import Cluster from './cluster';
import Location from './location';
import Visualization from '../../visualization';
// eslint-disable-next-line no-unused-vars
import style from './style.less';

const swap = ([a, b]) => [b, a];

class MapVisual extends frontendDateFilterable(Visualization) {
  getMaxAmount() {
    return Math.max(0, ...this.getData().map(pluck('amount')));
  }

  getTiles () {
    return (
      <TileLayer
        url="//{s}.tile.osm.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="//osm.org/copyright">OpenStreetMap</a> contributors'
      />
    );
  }

  render() {
    const { translations, filters, years, styling, monthly, months, zoom, data } = this.props;
    let center;
    let _zoom;
    if (data){
      center = L.latLngBounds(this.getData().map(pluck('coords')).map(swap)).getCenter();
      _zoom = zoom;
    } else {
      center = [0, 0];
      _zoom = 1;
    }

    return (
      <Map center={center} zoom={_zoom}>
        {this.getTiles()}
        <Cluster maxAmount={this.getMaxAmount()}>
          {this.getData().map(location => (
            <this.constructor.Location
              key={location._id}
              position={location.coords.reverse()}
              maxAmount={this.getMaxAmount()}
              data={location}
              translations={translations}
              filters={filters}
              years={years}
              monthly={monthly}
              months={months}
              styling={styling}
            />
          ))}
        </Cluster>
      </Map>
    );
  }
}

MapVisual.propTypes = {};
MapVisual.computeComparisonYears = null;
MapVisual.Location = Location;

export default MapVisual;
