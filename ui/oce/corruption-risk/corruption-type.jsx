import URI from "urijs";
import {Map} from "immutable";
import {pluckImm} from "../tools";
import CustomPopupChart from "./custom-popup-chart";

class IndicatorTile extends CustomPopupChart{
  getCustomEP(){
    const {indicator} = this.props;
    return `flags/${indicator}/stats`;
  }

  getData(){
    const data = super.getData();
    if(!data) return [];
    const sortedData = data.sort((a, b) => a.get('year') - b.get('year'));
    return [{
      x: sortedData.map(pluckImm('year')).toJS(),
      y: sortedData.map(pluckImm('totalTrue')).toJS(),
      type: 'scatter'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        type: 'category'
      }
    }
  }

  getPopup(){
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

class CorruptionType extends React.Component{
  constructor(...args){
    super(...args);
    this.state = {}
  }

  render(){
    const {indicators} = this.props;
    return (
      <div className="page-corruption-type">
	    {indicators.map((indicator, index) => (
        <div className="col-sm-4">
          <IndicatorTile
    		      key={index}
		          indicator={indicator}
    		      translations={{}}
			        filters={Map()}
		          requestNewData={(_, data) => this.setState({[indicator]: data})}
              data={this.state[indicator]}
    	    />
        </div>
	    ))}
      </div>
    )
  }
}

export default CorruptionType;
