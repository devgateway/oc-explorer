import MultipleSelect from "../../oce/filters/inputs/multiple-select";

class Locations extends MultipleSelect{
  getTitle(){
    return this.__('Bid selection method');
  }

  getId(option){
    return option.get('id');
  }

  getLabel(option){
    return option.get('description');
  }
}

Locations.ENDPOINT = 'ocds/location/all';

export default Locations;