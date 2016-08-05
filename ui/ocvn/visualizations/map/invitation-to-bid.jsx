import Map from "../../../oce/visualizations/map";

class InvitationToBid extends Map{
  getData(){
    return []
  }

  static getLayerName(__){
    return __('Invitation to bid');
  }
}

export default InvitationToBid;