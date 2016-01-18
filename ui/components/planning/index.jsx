import React from "react";
import Component from "../pure-render-component";
import {Map, MarkerGroup} from "react-d3-map";
import {callFunc} from "../../tools";
import Popup from "./popup";
require("./style.less");

export default class Planning extends Component{
  render(){
    var {locations, width} = this.props;
    return (
        <div className="col-sm-12">
          <Map
              scale={18000}
              width={width}
              height={1000}
              center={[105, 14.5]}
          >
            <MarkerGroup
                data={{
                  "type": "FeatureCollection",
                  "features": locations.map(location => {
                    return {
                      "type": "Feature",
                      "properties": {
                        "name": location.name,
                        "amount": location.totalPlannedAmount
                      },
                      "geometry": location.coordinates
                    }
                  })
                }}
                markerClass= {"location"}
                onClick={callFunc('showPopup')}
                onCloseClick={callFunc('hidePopup')}
                popupContent={location => <Popup data={location.properties}/>}
            />
          </Map>
        </div>
    )
  }
}