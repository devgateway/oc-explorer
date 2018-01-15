import { range } from '../../tools';
import CustomPopupChart from '../custom-popup-chart';
import CRDPage from '../page';
import { colorLuminance, wireProps } from '../tools';
import ProcurementsTable from '../procurements-table';
import { POPUP_HEIGHT } from '../constants';

const TRACES = ['COLLUSION', 'FRAUD', 'RIGGING'];

class CorruptionType extends CustomPopupChart {
  groupData(data) {
    const grouped = {};
    TRACES.forEach((trace) => {
      grouped[trace] = {};
    });

    const { monthly } = this.props;
    data.forEach((datum) => {
      const type = datum.get('type');
      let date;
      if (monthly) {
        const month = datum.get('month');
        date = this.t(`general:months:${month}`);
      } else {
        date = datum.get('year');
      }
      grouped[type] = grouped[type] || {};
      grouped[type][date] = datum.toJS();
    });

    return grouped;
  }

  getData() {
    const data = super.getData();
    if (!data) return [];
    const { styling, months, monthly, years } = this.props;
    const grouped = this.groupData(data);
    return Object.keys(grouped).map((type, index) => {
      const dataForType = grouped[type];
      let values = [];
      let dates = [];
      if (monthly) {
        dates = range(1, 12)
          .filter(month => months.has(month))
          .map(month => this.t(`general:months:${month}`));

        values = dates.map(month => (dataForType[month] ? dataForType[month].flaggedCount : 0));
      } else if (years.count()) {
        dates = years.sort().toArray();
        values = dates.map(year => (dataForType[year] ? dataForType[year].flaggedCount : 0));
      } else {
        dates = Object.keys(dataForType).sort();
        values = dates.map(year => dataForType[year] ? dataForType[year].flaggedCount : 0);
      }

      if (dates.length === 1) {
        dates.unshift('');
        dates.push(' ');
        values.unshift(0);
        values.push(0);
      }

      return {
        x: dates,
        y: values,
        type: 'scatter',
        fill: 'tonexty',
        name: this.t(`crd:corruptionType:${type}:name`),
        fillcolor: styling.charts.traceColors[index],
        line: {
          color: colorLuminance(styling.charts.traceColors[index], -0.3),
        },
      };
    });
  }

  getLayout() {
    return {
      hovermode: 'closest',
      xaxis: {
        type: 'category',
      },
      yaxis: {},
      legend: {
        orientation: 'h',
        xanchor: 'right',
        yanchor: 'bottom',
        x: 1,
        y: 1,
      },
    };
  }

  getPopup() {
    const { popup } = this.state;
    const { year, traceIndex } = popup;
    const corruptionType = TRACES[traceIndex];
    const { indicatorTypesMapping } = this.props;
    const data = this.groupData(super.getData());
    if (!data[corruptionType]) return null;
    const dataForPoint = data[corruptionType][year];
    if (!dataForPoint) return null;
    const indicatorCount =
      Object.keys(indicatorTypesMapping).filter(indicatorId =>
        indicatorTypesMapping[indicatorId].types.indexOf(dataForPoint.type) > -1
      ).length;

    const percentFlaggedLabel = this.t('crd:overview:overTimeChart:percentFlagged');

    let height = POPUP_HEIGHT;
    let { top } = popup;
    if (percentFlaggedLabel.length > 30) {
      const delta = 30;
      height += delta;
      if (popup.toTheLeft) {
        top += delta / 2;
      }
      top -= delta;
    }

    return (
      <div className="crd-popup" style={{ top, left: popup.left, height }}>
        <div className="row">
          <div className="col-sm-12 info text-center">
            {year}
          </div>
          <div className="col-sm-12">
            <hr />
          </div>
          <div className="col-sm-7 text-right title">{this.t('crd:overview:overTimeChart:indicators')}</div>
          <div className="col-sm-5 text-left info">{indicatorCount}</div>
          <div className="col-sm-7 text-right title">{this.t('crd:overview:overTimeChart:totalFlags')}</div>
          <div className="col-sm-5 text-left info">{dataForPoint.flaggedCount}</div>
          <div className="col-sm-7 text-right title">{this.t('crd:overview:overTimeChart:totalProcurementsFlagged')}</div>
          <div className="col-sm-5 text-left info">{dataForPoint.flaggedProjectCount}</div>
          <div className="col-sm-7 text-right title">{percentFlaggedLabel}</div>
          <div className="col-sm-5 text-left info">{dataForPoint.percent.toFixed(2)}%</div>
        </div>
        <div className="arrow" />
      </div>
    );
  }
}

CorruptionType.endpoint = 'percentTotalProjectsFlaggedByYear';

class OverviewPage extends CRDPage {
  constructor(...args) {
    super(...args);
    this.state = {
      topFlaggedContracts: null,
    };
  }

  render() {
    const { indicatorTypesMapping, styling, width, navigate } = this.props;
    return (
      <div className="page-overview">
        <section className="chart-corruption-types">
          <h3 className="page-header">{this.t('crd:overview:overTimeChart:title')}</h3>
          <CorruptionType
            {...wireProps(this, 'corruptionType')}
            styling={styling}
            indicatorTypesMapping={indicatorTypesMapping}
            width={width - 20}
            margin={{ t: 0, b: 40, r: 40, pad: 20 }}
          />
        </section>
        <section>
          <h3 className="page-header">{this.t('crd:overview:topFlagged:title')}</h3>
          <ProcurementsTable
            {...wireProps(this, 'topFlaggedContracts')}
            dataEP="corruptionRiskOverviewTable"
            countEP="corruptionRiskOverviewTable/count"
            navigate={navigate}
          />
        </section>
      </div>
    );
  }
}

export default OverviewPage;
