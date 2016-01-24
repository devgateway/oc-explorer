"use strict"

import {
  default as React,
  Component,
  PropTypes
} from 'react'

import Circle from "./circle";

var toK = number => number >= 1000 ? Math.round(number/1000) + "K" : number;

export default class CircleCollection extends Component {
  shouldComponentUpdate(nextProps, nextState) {
    if(nextProps.showPopup.size
      !== this.props.showPopup.size) {
      return false;
    }else {
      return true;
    }
  }

  render() {
    const {data, projection, onClick, onMouseOver, onMouseOut, circleClass} = this.props;
    var circles;
    var pointData;

    if(data.type === 'FeatureCollection') {
      pointData = [];

      // loop through features
      data.features.forEach(function(d) {
        pointData.push(d);
      });

    }else if(data.type === 'Feature') {

      pointData = data;
    }

    if(pointData) {
      // if not array, make it as array
      if(!Array.isArray(pointData))
        pointData = [pointData];

      var maxAmount = Math.max(0, ...pointData.map(data => data.properties.amount));

      circles = pointData.map((d, i) => {
        var x = +projection(d.geometry.coordinates)[0];
        var y = +projection(d.geometry.coordinates)[1];
        var id = x + '-' + y;
        var amountRatio = d.properties.amount / maxAmount;
        var green = Math.round(255 - 255 * amountRatio);
        return (
          <g
              key= {i}
              onClick={_ => onClick(this, d, i)}
          >
            <Circle
                color={`rgb(255, ${green}, 0)`}
                id= {id}
                data= {d}
                x= {x}
                y= {y}
                r={16}
                circleClass= {circleClass}
                onMouseOver= {onMouseOver}
                onMouseOut= {onMouseOut}
            />
            <text x={x} y={y + 5} textAnchor="middle" fill="black">{toK(d.properties.count)}</text>
          </g>
        )
      })
    }

    return (
      <g>
        {circles}
      </g>
    )
  }
}
