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
                  "features": locations
                    .groupBy(location => location.get('_id'))
                    .map(locations => locations.reduce((reducedLocation, location) => {
                        return {
                            "type": "Feature",
                            "properties": {
                              "name": location.get('name'),
                              "amount": reducedLocation.properties.amount + location.get('totalPlannedAmount'),
                              "count": reducedLocation.properties.count + location.get('recordsCount')
                            },
                            "geometry": location.get('coordinates').toJS()
                        }
                    }, {
                        "properties": {
                          "amount": 0,
                          "count": 0
                        }
                    })).toArray()
                }}
                circleClass= {"location"}
                onClick={callFunc('showPopup')}
                onCloseClick={callFunc('hidePopup')}
                PopupComponent={Popup}
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