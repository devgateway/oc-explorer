import Map from "./index.jsx";

class TenderLocations extends Map{
  getData(){
    return []
  }

  static getLayerName(__){
    return __('Tender locations');
  }
}

TenderLocations.endpoint = 'fundingByTenderDeliveryLocation';

export default TenderLocations;