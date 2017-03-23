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
    const flaggedStats = entry.get('flaggedStats');
    return (
      <tr key={index}>
        <td></td>
        <td>{entry.get('ocid')}</td>
        <td>{entry.get('title')}</td>
        <td>{entry.getIn(['procuringEntity', 'name'])}</td>
        <td>{tenderValue.get('amount')} {tenderValue.get('currency')}</td>
        <td>{awardValue.get('amount')} {awardValue.get('currency')}</td>
        <td>{startDate.toLocaleDateString()}&mdash;{endDate.toLocaleDateString()}</td>
        <td>{flaggedStats.get('type')}</td>
        <td>{flaggedStats.get('count')}</td>
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
            <th>Number of risk type flags</th>
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
          <h4>The Projects with the Most Fraud, Collusion and Process Rigging Flags</h4>
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