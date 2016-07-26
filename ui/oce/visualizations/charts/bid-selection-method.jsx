import FrontendYearFilterableChart from "./frontend-filterable";
import {pluckImm, max} from "../../tools";
import {Map, Set} from "immutable";
import Comparison from "../../comparison";
import Plotly from "plotly.js/lib/core";

class BidSelectionMethod extends FrontendYearFilterableChart{
  static toCats(data){
    if(!data) return Map();
    return data
      .groupBy(pluckImm('procurementMethodDetails'))
      .map((bidTypes, _id) => Map({
        _id,
        totalTenderAmount: bidTypes.map(pluckImm('totalTenderAmount')).reduce((a, b) => a + b, 0)
      }));
  }

  getCats(data){
    let cats = this.props.cats || Map();
    return cats.merge(this.constructor.toCats(data));
  }

  getData(){
    let raw = super.getData();
    if(!raw) return [];
    let data = this.getCats(raw);

    return [{
      x: data.map(imm => imm.get('_id') || this.__('Unspecified')).toArray(),
      y: data.map(pluckImm('totalTenderAmount')).toArray(),
      type: 'bar'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: this.__("Method"),
        type: "category",
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: this.__("Amount"),
        titlefont: {
          color: "#cc3c3b"
        }
      }
    }
  }
}

BidSelectionMethod.endpoint = 'tenderPriceByVnTypeYear';
BidSelectionMethod.getName = __ => __('Bid selection method');
BidSelectionMethod.UPDATABLE_FIELDS = ['data', 'years', 'cats'];

class BidSelectionMethodComparison extends Comparison{
  render(){
    let {compareBy, comparisonData, comparisonCriteriaValues, filters, requestNewComparisonData, years} = this.props;
    if(!comparisonCriteriaValues.length) return null;
    let Component = this.getComponent();
    let decoratedFilters = this.constructor.decorateFilters(filters, compareBy, comparisonCriteriaValues);
    let rangeProp, cats;

    if(comparisonData.count() == comparisonCriteriaValues.length + 1){
      let uniformData = comparisonData.map(comparisonDatum =>
          Component.toCats(Component.filterDataByYears(comparisonDatum, years))
      ).reduce(
          (a, b) => a.mergeWith(
              (a, b) => a.get('totalTenderAmount') > b.get('totalTenderAmount') ? a : b
          , b)
      );

      let maxValue = uniformData.map(pluckImm('totalTenderAmount')).reduce(max, 0);

      rangeProp = {
        yAxisRange: [0, maxValue]
      };

      cats = uniformData.map(uniformDatum => uniformDatum.set('totalTenderAmount', undefined))
    } else {
      cats = Map();
      rangeProp = {};
    }

    return this.wrap(decoratedFilters.map((comparisonFilters, index) => <div className="col-md-6" key={index}>
          <Component
              filters={comparisonFilters}
              requestNewData={(_, data) => requestNewComparisonData([index], data)}
              data={comparisonData.get(index)}
              years={years}
              title={this.getTitle(index)}
              cats={cats}
              {...rangeProp}
          />
        </div>
    ));
  }
}


BidSelectionMethod.compareWith = BidSelectionMethodComparison;

export default BidSelectionMethod;