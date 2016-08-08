import Map from "../index.jsx";
import Location from './location';

class TenderLocations extends Map{
  transform(data){
    return data.filter(location => {
      if(!location['items.deliveryLocation'].geometry){
        console.warn("Invalid delivery location! Missing geometry!", location);
        return false;
      }
      return true;
    });
  }

  getData(){
    let data = super.getData();
    if(!data) return [];
    return data
        .groupBy(location => location.getIn(['items.deliveryLocation', '_id']))
        .map(locations => locations.reduce((reducedLocation, location) => {
          return {
            "_id": location.getIn(['items.deliveryLocation', '_id']),
            "name": location.getIn(['items.deliveryLocation', 'description']),
            "amount": reducedLocation.amount + location.get('totalTendersAmount'),
            "count": reducedLocation.count + location.get('tendersCount'),
            "coords": location.getIn(['items.deliveryLocation', 'geometry', 'coordinates']).toJS()
          }
        }, {
          "amount": 0,
          "count": 0
        }))
        .toArray()
  }

  static getLayerName(__){
    return __('Tender locations');
  }
}

TenderLocations.endpoint = 'fundingByTenderDeliveryLocation';
TenderLocations.Location = Location;

export default TenderLocations;