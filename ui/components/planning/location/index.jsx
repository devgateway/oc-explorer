import Marker from "./marker";
import Component from  "../../pure-render-component";
import {Popup} from "react-leaflet";
import {toK} from "../../../tools";

export default class LocationWrapper extends Component{
  render(){
    var {amount, name} = this.props.data;
    return (
        <Marker {...this.props}>
          <Popup>
            <div>
              <h3>{name}</h3>
              <p>
                <strong>Total Planned Amount (VND):</strong> {amount.toLocaleString()}
              </p>
            </div>
          </Popup>
        </Marker>
    )
  }
}

