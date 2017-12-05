import Plotly from "plotly.js/lib/core";
import Chart from "../visualizations/charts/index.jsx";
import {pluckImm, debounce} from "../tools";
import backendYearFilterable from "../backend-year-filterable";
import Visualization from "../visualization";
import { fromJS, Set } from "immutable";
import translatable from "../translatable";

Plotly.register([
  require('plotly.js/lib/pie')
]);

const EMPTY_SET = Set();

class TotalFlagsChart extends backendYearFilterable(Chart){
  getData(){
    const data = super.getData();
    if(!data || !data.count()) return [];
    const labels = data.map(datum => this.t(`crd:corruptionType:${datum.get('type')}:name`)).toJS();
    return [{
      values: data.map(pluckImm('indicatorCount')).toJS(),
      labels: labels,
      textinfo: 'value',
      hole: .8,
      type: 'pie',
      hoverlabel: {
        bgcolor: '#144361'
      },
      marker: {
        colors: ['#fac329', '#289df5', '#3372b1']//if you change this colors you'll have to also change it for the custom legend in ./style.less
      },
      outsidetextfont: {
        size: 15,
        color: '#3fc529'
      },
      insidetextfont: {
        size: 15,
        color: '#3fc529'
      },
    }];
  }

  getLayout(){
    const {width} = this.props;
    return {
      showlegend: false,
      paper_bgcolor: 'rgba(0,0,0,0)'
    }
  }
}

TotalFlagsChart.endpoint = 'totalFlaggedIndicatorsByIndicatorType';

class Counter extends backendYearFilterable(Visualization) {
  render() {
    const {data} = this.props;
    if(!data) return null;
    return (
      <div className="counter">
        <div className="count text-right">
          {this.getCount()}
        </div>
        <div className="text text-left">
          {this.getTitle()}
        </div>
      </div>
    );
  }
};

class FlagsCounter extends Counter {
  getTitle() {
    return this.t('crd:charts:totalFlags:title');
  }

  getCount() {
    const { data } = this.props;
    return data.getIn([0, 'flaggedCount'], 0);
  }
}

FlagsCounter.endpoint = 'totalFlags';

class ContractCounter extends Counter {
  getTitle() {
    return this.t('crd:sidebar:totalContracts:title');
  }

  getCount() {
    return this.props.data;
  }
}

ContractCounter.endpoint = 'ocds/release/count';

class TotalFlags extends translatable(React.Component){
  constructor(...args){
    super(...args);
    this.state = {
    }

    this.updateSidebarWidth = debounce(() =>
      this.setState({
        width: document.getElementById('crd-sidebar').offsetWidth
      })
    );
  }

  componentDidMount(){
    this.updateSidebarWidth();
    window.addEventListener("resize", this.updateSidebarWidth);
  }

  componentWillUnmount(){
    window.removeEventListener("resize", this.updateSidebarWidth)
  }

  render() {
    const { data, requestNewData, translations, filters, years, months, monthly, allYears } = this.props;
    const { width } = this.state;
    if(!width) return null;
    return (
      <div className="total-flags">
        <ContractCounter
          data={data.get('contractCounter')}
          requestNewData={(_, data) => requestNewData(['contractCounter'], data)}
          translations={translations}
          filters={filters}
          years={Set(allYears).equals(years) ? EMPTY_SET : years}
          months={months}
          monthly={monthly}
        />
        <FlagsCounter
          data={data.get('flagsCounter')}
          requestNewData={(_, data) => requestNewData(['flagsCounter'], data)}
          translations={translations}
          filters={filters}
          years={years}
          months={months}
          monthly={monthly}
        />
        <TotalFlagsChart
          data={data.get('chart')}
          requestNewData={(_, data) => requestNewData(['chart'], data)}
          translations={translations}
          width={width}
          height={200}
          margin={{l:0, r:0, t: 40, b: 20, pad:0}}
          filters={filters}
          years={years}
          months={months}
          monthly={monthly}
        />
        <div className="crd-legend">
          <div className="fraud">{this.t("crd:corruptionType:FRAUD:name")}</div>
          <div className="rigging">{this.t("crd:corruptionType:RIGGING:name")}</div>
          <div className="collusion">{this.t("crd:corruptionType:COLLUSION:name")}</div>
        </div>
      </div>
    )
  }
}

export default TotalFlags;
