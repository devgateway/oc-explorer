"use strict";

import {
  default as React,
  Component,
  PropTypes
} from 'react';

import d3 from 'd3';

import {
  default as ReactFauxDOM,
} from 'react-faux-dom';

class Circle extends Component {
  constructor(props) {
    super(props);
  }

  _mkCircle(dom) {
    const {data, circleClass, r, x, y, onMouseOut, onMouseOver, color} = this.props;

    var circle = d3.select(dom);

    circle
        .datum(data)
        .attr('fill', color)
        .attr('class', `${circleClass} bubble`)
        .attr("transform", (d) => { return `translate(${x}, ${y})`})
        .attr("r", r)
        .on("mouseover", function (d, i) {return onMouseOver(this, d, i);})
        .on("mouseout", function (d, i) {return onMouseOut(this, d, i);})

    return circle;
  }

  render() {
    var circle = ReactFauxDOM.createElement('circle');
    var chart = this._mkCircle(circle)

    return chart.node().toReact();
  }
}

Circle.defaultProps = {
  centroidClass: 'react-d3-map-core__centroid',
  dy: '.35em',
  onMouseOver: (d) => {},
  onMouseOut: (d) => {}
};

Circle.propTypes = {
  data: PropTypes.object.isRequired,
  circleClass: PropTypes.string
};

export default Circle;
