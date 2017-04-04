import Chart from "../visualizations/charts/index";
import URI from "urijs";
import {Map} from "immutable";
import {pluckImm} from "../tools";

class IndicatorTile extends Chart{
  getCustomEP(){
    const {indicator} = this.props;
    return `flags/${indicator}/stats`;
  }

  getData(){
    const data = super.getData();
    if(!data) return [];
    return [{
      x: data.map(pluckImm('year')).toJS(),
      y: data.map(pluckImm('totalTrue')).toJS(),
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
