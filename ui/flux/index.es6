import dispatcher from "./dispatcher";
import actions from "./actions";
import globalStateStore from "./stores/global-state";
import {toImmutable} from "nuclear-js";
import {Map} from "immutable";
import {pluck, identity} from '../tools';

dispatcher.registerStores({
  globalState: globalStateStore
});

var ensureObject = maybeObject => "object" == typeof maybeObject ? maybeObject : {};

var ensureArray = maybeArray => Array.isArray(maybeArray) ? maybeArray : [];

var obj2arr = obj => Object.keys(obj).map(key => obj[key]);

var getSg = pl => pl.replace(/s$/, "");

var getSelectedYears = [
  ['globalState', 'filters', 'years'],
  years => years ? years.filter(identity).keySeq().toArray() : []
];

var yearOnly = year => ({
  year: year
});

var mkDataGetter = ({path, getFillerDatum = yearOnly}) => [
  ['globalState', 'compareBy'],
  ['globalState', 'data', path],
  ['globalState', 'comparisonData', path],
  getSelectedYears,
  (compare, rawData, comparisonData, years) => {
    var parse = data => {
      var dataByYear = [];
      years.forEach(year => dataByYear[year] = getFillerDatum(year));
      data.forEach(datum => {
        if(dataByYear[+datum.year]) dataByYear[+datum.year] = datum
      });
      return obj2arr(dataByYear);
    };
    if(compare){
      var data = ensureArray(comparisonData).map(parse);
      var maxValue = Math.max.apply(Math, data.map(datum =>
          Math.max.apply(Math, Object.keys(datum).filter(key => key !='year').map(key => datum[key])))
      );
      return {
        yAxisRange: [0, maxValue],
        data: data
      }
    } else {
      return parse(ensureArray(rawData));
    }
  }
];

var getOverviewData = mkDataGetter({
  path: "overview"
});

var getOverview = [
    ['globalState', 'compareBy'],
    getOverviewData,
    ['globalState', 'data', 'topTenders'],
    ['globalState', 'data', 'topAwards'],
    (compare, overviewData, topTenders, topAwards) => {
      return {
        compare: compare,
        overview: overviewData,
        topTenders: topTenders,
        topAwards: topAwards
      }
    }
];

var getCostEffectiveness = [
  ['globalState', 'compareBy'],
  ['globalState', 'data', 'costEffectiveness'],
  ['globalState', 'comparisonData', 'costEffectiveness'],
  getSelectedYears,
  (compare, rawData, comparisonData, years) => {
    var parse = data => {
      var dataByYear = [];
      years.forEach(year => dataByYear[year] = {
        year: year,
        tender: 0,
        diff: 0
      });
      data.forEach(datum => {
        if(dataByYear[+datum.year]) dataByYear[+datum.year] = datum
      });
      return obj2arr(dataByYear);
    };
    if(compare){
      var data = ensureArray(comparisonData).map(parse);
      var maxValue = Math.max.apply(Math, data.map(({diff, tender}) => Math.max(diff, tender)));
      return {
        data: data,
        yAxisrange: [0, maxValue]
      };
    } else {
      return parse(ensureArray(rawData));
    }
  }
]

var getTender = [
    ['globalState', 'compareBy'],
    getCostEffectiveness,
    (compare, costEffectiveness) => {
      return {
        compare: compare,
        costEffectiveness: costEffectiveness
      }
    }
]

var getGlobalState = [
    [],
    state => state
        .set('overview', dispatcher.evaluate(getOverview))
        .set('tender', dispatcher.evaluate(getTender))
];

export default {
  actions: actions,
  onUpdate(cb, trigger = false){
    dispatcher.observe(getGlobalState, cb);
    if(trigger) cb(dispatcher.evaluate(getGlobalState));
  }
}