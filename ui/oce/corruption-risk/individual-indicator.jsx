import CustomPopupChart from "./custom-popup-chart";
import {Map} from "immutable";
import {pluckImm} from "../tools";

class IndividualIndicatorChart extends CustomPopupChart{
  getCustomEP(){
    const {indicator} = this.props;
    return `flags/${indicator}/stats`;
  }

  getData(){
    const data = super.getData();
    if(!data) return [];
    const sortedData = data.sort((a, b) => a.get('year') - b.get('year'));
    const years = sortedData.map(pluckImm('year')).toJS();
    return [{
      x: years,
      y: sortedData.map(pluckImm('totalTrue')).toJS(),
      type: 'scatter',
      fill: 'tozerox',
      name: 'Flagged',
      hoverinfo: 'name',
      fillcolor: '#85cbfe',
      line: {
        color: '#85cbfe'
      }
    }, {
      x: years,
      y: sortedData.map(pluckImm('totalPrecondMet')).toJS(),
      type: 'scatter',
      fill: 'tonexty',
      name: 'Eligible to be flagged',
      hoverinfo: 'name',
      fillcolor: '#336ba6',
      line: {
        color: '#336ba6'
      }
    }];
  }

  getLayout(){
    return {
      showlegend: false,
      hovermode: 'closest',
      xaxis: {
        type: 'category',
        showgrid: false
      }
    }
  }

  getPopup(){
    const {indicator} = this.props;
    const {popup} = this.state;
    const {year} = popup;
    const data = super.getData();
    const datum = data.find(datum => datum.get('year') == year);
    return (
      <div className="crd-popup" style={{top: popup.top, left: popup.left}}>
        <div className="row">
          <div className="col-sm-12 info text-center">
            {year}
          </div>
          <div className="col-sm-12">
            <hr/>
          </div>
          <div className="col-sm-7 text-right title">Projects Flagged</div>
          <div className="col-sm-5 text-left info">{datum.get('totalTrue')}</div>
          <div className="col-sm-7 text-right title">Eligible Projects</div>
          <div className="col-sm-5 text-left info">{datum.get('totalPrecondMet')}</div>
          <div className="col-sm-7 text-right title">Eligible Projects %</div>
          <div className="col-sm-5 text-left info">{datum.get('percentPrecondMet').toFixed(2)} %</div>
          <div className="col-sm-7 text-right title">Total Eligible %</div>
          <div className="col-sm-5 text-left info">{datum.get('percentTruePrecondMet').toFixed(2)} %</div>
        </div>
        <div className="arrow"/>
      </div>
    )
  }
}


class IndividualIndicatorPage extends React.Component{
  constructor(...args){
    super(...args);
    this.state = {
    }
  }

  render(){
    const {chart} = this.state;
    const {indicator} = this.props;
    return (
      <div className="page-corruption-type">
        <h4>Individual Indicator Page</h4>
        <IndividualIndicatorChart
            indicator={indicator}
            translations={{}}
            filters={Map()}
            requestNewData={(_, data) => this.setState({chart: data})}
            data={chart}
        />
      </div>
    )
  }
}

export default IndividualIndicatorPage;
