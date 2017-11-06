import URI from "urijs";
import {Map} from "immutable";
import {pluckImm} from "../tools";
import CustomPopupChart from "./custom-popup-chart";
import translatable from '../translatable';
import CRDPage from "./page";
import { colorLuminance } from './tools';
import Crosstab from './crosstab';

class IndicatorTile extends CustomPopupChart{
  getCustomEP(){
    const {indicator} = this.props;
    return `flags/${indicator}/stats`;
  }

  getData(){
    const color = this.props.styling.charts.traceColors[2];
    const data = super.getData();
    if(!data) return [];
    const {monthly} = this.props;
    let dates = monthly ?
      data.map(datum => {
        const month = datum.get('month');
        return this.t(`general:months:${month}`);
      }).toJS() :
      data.map(pluckImm('year')).toJS();

    let values = data.map(pluckImm('totalTrue')).toJS();

    //OCE-273, preventing the chart from showing only a dot
    if(dates.length == 1){
      dates.unshift("");
      dates.push(" ");
      values.unshift(0);
      values.push(0);
    }

    return [{
      x: dates,
      y: values,
      hoverinfo: 'none',
      type: 'scatter',
      fill: 'tonexty',
      fillcolor: color,
      line: {
        color: colorLuminance(color, -.3)
      }
    }];
  }

  getLayout(){
    const data = super.getData();
    const maxValue = data ?
      data.reduce((max, datum) => Math.max(max, datum.get('totalTrue')), 0) :
      0;

    return {
      xaxis: {
        type: 'category',
        showgrid: false,
        showline: false,
        tickangle: -60
      },
      yaxis: {
        tickangle: -90,
        tickmode: 'linear',
        tick0: 0,
        dtick: maxValue / 2
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
          <div className="col-sm-8 text-right title">{this.t('crd:corruptionType:indicatorTile:procurementsFlagged')}</div>
          <div className="col-sm-4 text-left info">{datum.get('totalTrue')}</div>
          <div className="col-sm-8 text-right title">{this.t('crd:corruptionType:indicatorTile:eligibleProcurements')}</div>
          <div className="col-sm-4 text-left info">{datum.get('totalPrecondMet')}</div>
          <div className="col-sm-8 text-right title">{this.t('crd:corruptionType:indicatorTile:percentEligibleFlagged')}</div>
          <div className="col-sm-4 text-left info">{datum.get('percentTruePrecondMet').toFixed(2)} %</div>
          <div className="col-sm-8 text-right title">{this.t('crd:corruptionType:indicatorTile:percentProcurementsEligible')}</div>
          <div className="col-sm-4 text-left info">{datum.get('percentPrecondMet').toFixed(2)} %</div>
        </div>
        <div className="arrow"/>
      </div>
    )
  }
}


function groupBy3(arr){
  if(arr.length == 0) return [];
  if(arr.length <= 3) return [arr];
  return [arr.slice(0, 3)].concat(groupBy3(arr.slice(3)));
}

class CorruptionType extends translatable(CRDPage){
  constructor(...args){
    super(...args);
    this.state = {
      indicatorTiles: {}
    }
  }

  updateIndicatorTile(indicator, data){
    let {indicatorTiles} = this.state;
    indicatorTiles[indicator] = data;
    this.setState({indicatorTiles})
  }

  componentDidUpdate(prevProps){
    if(this.props.corruptionType != prevProps.corruptionType){
      this.scrollTop();
    }
  }

  render(){
    const {indicators, onGotoIndicator, corruptionType, filters, years, monthly, months,
           translations, width, styling} = this.props;
    const {crosstab, indicatorTiles} = this.state;
    if(!indicators || !indicators.length) return null;

    return (
      <div className="page-corruption-type">
        <h2 className="page-header">{this.t(`crd:corruptionType:${corruptionType}:pageTitle`)}</h2>
        <p className="introduction" dangerouslySetInnerHTML={{__html: this.t(`crd:corruptionType:${corruptionType}:introduction`)}}/>
        {groupBy3(indicators).map(row => {
           return (
             <div className="row">
               {row.map(indicator => {
                  const indicatorName = this.t(`crd:indicators:${indicator}:name`);
                  const indicatorDescription = this.t(`crd:indicators:${indicator}:shortDescription`);
                  return (
                    <div className="col-sm-4 indicator-tile-container" key={corruptionType+indicator} onClick={e => onGotoIndicator(indicator)}>
                      <div className="border">
                        <h4>{indicatorName}</h4>
                        <p>{indicatorDescription}</p>
                        <IndicatorTile
                          indicator={indicator}
                          translations={translations}
                          filters={filters}
                          requestNewData={(_, data) => this.updateIndicatorTile(indicator, data)}
                          data={indicatorTiles[indicator]}
                          margin={{t: 10, r: 5, b: 50, l: 20, pad: 5}}
                          height={300}
                          years={years}
                          monthly={monthly}
                          months={months}
                          width={width/3-60}
                          styling={styling}
                        />
                      </div>
                    </div>
                  )
               })}
             </div>
           )
        })}
        <section>
          <h3 className="page-header">
            {this.t(`crd:corruptionType:${corruptionType}:crosstabTitle`)}
          </h3>
          <p className="introduction">{this.t(`crd:corruptionType:${corruptionType}:crosstab`)}</p>
          <Crosstab
            filters={filters}
            years={years}
            monthly={monthly}
            months={months}
            indicators={indicators}
            data={crosstab}
            requestNewData={(_, data) => this.setState({crosstab: data})}
            translations={translations}
          />
        </section>
      </div>
    )
  }
}

export default CorruptionType;
