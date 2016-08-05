import {Marker} from "react-leaflet";

export default class Location extends Marker{
  componentDidUpdate(prevProps){
    super.componentDidUpdate(prevProps);
    if(prevProps.data != this.props.data){
      this.leafletElement.options.data = this.props.data;
    }
  }
}
