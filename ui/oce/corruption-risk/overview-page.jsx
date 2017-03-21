import Chart from "../visualizations/charts/index";
import {Map} from "immutable";
import {pluck} from "../tools";
import Table from "../visualizations/tables/index";

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

class TopFlaggedContracts extends Table{
  row(entry, index){
    const tenderValue = entry.getIn(['tender', 'value']);
    const awardValue = entry.getIn(['awards', 0, 'value']);
    const tenderPeriod = entry.get('tenderPeriod');
    const startDate = new Date(tenderPeriod.get('startDate'));
    const endDate = new Date(tenderPeriod.get('endDate'));
    return (
        <tr key={index}>
          <th></th>
          <th>{entry.get('ocid')}</th>
          <th>{entry.get('title')}</th>
          <th>{entry.getIn(['procuringEntity', 'name'])}</th>
          <th>{tenderValue.get('amount')} {tenderValue.get('currency')}</th>
          <th>{awardValue.get('amount')} {awardValue.get('currency')}</th>
          <th>{startDate.toLocaleDateString()}&mdash;{endDate.toLocaleDateString()}</th>
          <th>{entry.getIn(['flaggedStats', 'type'])}</th>
        </tr>
    )
  }

  render(){
    const {data} = this.props;
    return (
        <table className="table table-striped table-hover table-top-flagged-contracts">
          <thead>
          <tr>
            <th>Status</th>
            <th>Contract ID</th>
            <th>Title</th>
            <th>Procuring Entity</th>
            <th>Tender Amount</th>
            <th>Awards Amount</th>
            <th>Tender Date</th>
            <th className="flag-type">Flag Type</th>
          </tr>
          </thead>
          <tbody>
          {data && data.map(this.row.bind(this))}
          </tbody>
        </table>
    )
  }
}

TopFlaggedContracts.endpoint = 'corruptionRiskOverviewTable?pageSize=10';

class OverviewPage extends React.Component{
  constructor(...args){
    super(...args);
    this.state = {
      corruptionType: null,
      topFlaggedContracts: null
    }
  }

  render(){
    const {corruptionType, topFlaggedContracts} = this.state;
    return (
      <div>
        <section>
          <h4>Corruption Types</h4>
          <CorruptionType
              filters={Map()}
              requestNewData={(_, corruptionType) => this.setState({corruptionType})}
              translations={{}}
              data={corruptionType}
          />
        </section>
        <section>
          <TopFlaggedContracts
              filters={Map()}
              data={topFlaggedContracts}
              translations={{}}
              years={Map()}
              requestNewData={(_, topFlaggedContracts) => this.setState({topFlaggedContracts})}
          />
        </section>
      </div>
    )
  }
}

export default OverviewPage;