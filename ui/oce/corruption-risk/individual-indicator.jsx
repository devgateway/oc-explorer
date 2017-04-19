import CustomPopupChart from "./custom-popup-chart";
import {Map} from "immutable";
import {pluckImm} from "../tools";
import Table from "../visualizations/tables/index";
import INDICATOR_NAMES from "./indicator-names";

class IndividualIndicatorChart extends CustomPopupChart{
  getCustomEP(){
    const {indicator} = this.props;
    return `flags/${indicator}/stats`;
  }

  getData(){
    const data = super.getData();
    if(!data) return [];
		const {monthly} = this.props;
		const dates = monthly ?
									data.map(datum => {
										const month = datum.get('month');
										return this.t(`general:months:${month}`);
									}).toJS() :
									data.map(pluckImm('year')).toJS();
    return [{
      x: dates,
      y: data.map(pluckImm('totalTrue')).toJS(),
      type: 'scatter',
      fill: 'tonexty',
      name: 'Flagged',
      hoverinfo: 'name',
      fillcolor: '#85cbfe',
      line: {
        color: '#63a0cd'
      }
    }, {
      x: dates,
      y: data.map(pluckImm('totalPrecondMet')).toJS(),
      type: 'scatter',
      fill: 'tonexty',
      name: 'Eligible to be flagged',
      hoverinfo: 'name',
      fillcolor: '#336ba6',
      line: {
        color: '#224a74'
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
    const {indicator, monthly} = this.props;
    const {popup} = this.state;
    const {year} = popup;
    const data = super.getData();
		if(!data) return null;
		let datum;
		if(monthly){
			datum = data.find(datum => {
				const month = datum.get('month');
				return year == this.t(`general:months:${month}`);
			})
		} else {
			datum = data.find(datum => datum.get('year') == year);
		}
    return (
      <div className="crd-popup" style={{top: popup.top, left: popup.left}}>
        <div className="row">
          <div className="col-sm-12 info text-center">
            {year}
          </div>
          <div className="col-sm-12">
            <hr/>
          </div>
          <div className="col-sm-8 text-right title">Projects Flagged</div>
          <div className="col-sm-4 text-left info">{datum.get('totalTrue')}</div>
          <div className="col-sm-8 text-right title">Eligible Projects</div>
          <div className="col-sm-4 text-left info">{datum.get('totalPrecondMet')}</div>
          <div className="col-sm-8 text-right title">% Eligible Projects Flagged</div>
          <div className="col-sm-4 text-left info">{datum.get('percentTruePrecondMet').toFixed(2)} %</div>
          <div className="col-sm-8 text-right title">% Projects Eligible</div>
          <div className="col-sm-4 text-left info">{datum.get('percentPrecondMet').toFixed(2)} %</div>
        </div>
        <div className="arrow"/>
      </div>
    )
  }
}

class ProjectTable extends Table{
  getCustomEP(){
    const {indicator} = this.props;
    return `flags/${indicator}/releases?pageSize=10`;
  }

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
      </tr>
    )
  }

  render(){
    const {data} = this.props;
    if(!data) return null;
    return (
      <table className="table table-striped table-hover table-project-table">
        <thead>
          <tr>
            <th>Status</th>
            <th>Contract ID</th>
            <th>Title</th>
            <th>Procuring Entity</th>
            <th>Tender Amount</th>
            <th>Award Amount</th>
            <th>Tender Date</th>
            <th>Flag Type</th>
          </tr>
        </thead>
        <tbody>
          {data && data.map(this.row.bind(this))}
        </tbody>
      </table>
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
    const {chart, table} = this.state;
    const {indicator, translations, filters, years, monthly, months, width} = this.props;
    return (
      <div className="page-corruption-type">
        <h4>{INDICATOR_NAMES[indicator].name}</h4>
        <p className="definition">{INDICATOR_NAMES[indicator].indicator}</p>
        <p className="definition">{INDICATOR_NAMES[indicator].eligibility}</p>
        <p className="definition">{INDICATOR_NAMES[indicator].thresholds}</p>
        <p className="definition">{INDICATOR_NAMES[indicator].description_text}</p>        <IndividualIndicatorChart
            indicator={indicator}
            translations={translations}
						filters={filters}
						years={years}
						monthly={monthly}
						months={months}
            requestNewData={(_, data) => this.setState({chart: data})}
            data={chart}
						width={width}
        />
        <ProjectTable
            indicator={indicator}
            requestNewData={(_, data) => this.setState({table: data})}
            data={table}
						translations={translations}
            filters={filters}
            years={years}
						monthly={monthly}
						months={months}
        />
      </div>
    )
  }
}

export default IndividualIndicatorPage;
