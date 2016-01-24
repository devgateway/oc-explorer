import React from "react";
import Component from "../pure-render-component";
import {Map} from "react-d3-map";
import CircleGroup from "./circle-group";
import {callFunc} from "../../tools";
import Popup from "./popup";
import {ZoomControl} from "react-d3-map-core";
require("./style.less");

const SCALE = 18000;

export default class Planning extends Component{
  constructor(props){
    super(props);
    this.state = {
      scale: SCALE
    }
  }

  render(){
    var {locations, width} = this.props;
    var {scale} = this.state;
    return (
        <div className="col-sm-12 content map-content">
          <Map
              margins={{top: 0, right: 0, bottom: 0, left: 0}}
              scale={SCALE}
              zoomScale={scale}
              width={width}
              height={1000}
              center={[105, 14.5]}
          >
            <CircleGroup
                data={{
                  "type": "FeatureCollection",
                  "features": locations.map(location => {
                    return {
                      "type": "Feature",
                      "properties": {
                        "name": location.name,
                        "amount": location.totalPlannedAmount,
                        "count": location.recordsCount
                      },
                      "geometry": location.coordinates
                    }
                  })
                }}
                circleClass= {"location"}
                onClick={callFunc('showPopup')}
                onCloseClick={callFunc('hidePopup')}
                popupContent={location => <Popup data={location.properties}/>}
            />
            <ZoomControl
                zoomInClick={() => this.setState({scale: scale * 2})}
                zoomOutClick={() => this.setState({scale: scale / 2})}
            />
          </Map>
        </div>
    )
  }
}