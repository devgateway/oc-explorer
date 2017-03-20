import Chart from "../visualizations/charts/index";
import {Map} from "immutable";
import {pluck} from "../tools";

class CorruptionType extends Chart{
  getData(){
    const data = super.getData();
    if(!data) return [];
    let grouped = {};

    data.forEach(datum => {
      const type = datum.get('type');
      const year = datum.get('year');
      grouped[type] = grouped[type] || [];
      grouped[type].push(datum.toJS());
    });

    return Object.keys(grouped).map(type => {
      const dataForType = grouped[type];
      return {
        x: dataForType.map(pluck('year')),
        y: dataForType.map(pluck('indicatorCount')),
        type: 'scatter',
        name: type
      }
    });
  }

  getLayout(){
    return {
      xaxis: {
        type: 'category'
      }
    }
  }
}

CorruptionType.endpoint = 'totalFlaggedIndicatorsByIndicatorTypeByYear';

class OverviewPage extends React.Component{
  constructor(...args){
    super(...args);
    this.state = {
      corruptionType: null
    }
  }

  render(){
    return (
        <section>
          <h4>Corruption Types</h4>
          <CorruptionType
              filters={Map()}
              requestNewData={(_, corruptionType) => this.setState({corruptionType})}
              translations={{}}
              data={this.state.corruptionType}
          />
        </section>
    )
  }
}

export default OverviewPage;