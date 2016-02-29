import Plot from "../plot";

export default class CostEffectiveness extends Plot{
  getTitle(){
    return  "Cost effectiveness";
  }

  getData(){
    var years = this.props.data.map(datum => datum.year);

    var bidPrice = {
      x: years,
      y: this.props.data.map(datum => datum.tender),
      name: 'Bid price',
      type: 'bar'
    };

    var diff = {
      x: years,
      y: this.props.data.map(datum => datum.diff),
      name: 'Difference',
      type: 'bar'
    };

    return [bidPrice, diff];
  }

  getLayout(){
    return {
      barmode: "stack",
      xaxis:{
        type: "category"
      }
    }
  }
}