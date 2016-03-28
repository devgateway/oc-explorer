import dispatcher from "./dispatcher";
import actions from "./actions";
import globalStateStore from "./stores/global-state";
import {toImmutable} from "nuclear-js";
import {Map} from "immutable";
import {pluck, identity} from '../tools';

dispatcher.registerStores({
  globalState: globalStateStore
});

var ensureArray = maybeArray => Array.isArray(maybeArray) ? maybeArray : [];

var obj2arr = obj => Object.keys(obj).map(key => obj[key]);

var getSelectedYears = [
  ['globalState', 'filters', 'years'],
  years => years ? years.filter(identity).keySeq().toArray() : []
];

var yearOnly = year => ({
  year: year
});

var mkDataGetter = ({path, getFillerDatum = yearOnly, horizontal = false}) => [
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
      var arrOfData = ensureArray(comparisonData).map(parse);
      //we have an array of arrays of objects if certain fields, we need the max value of those fields
      var maxValue = Math.max.apply(Math, arrOfData.map(data =>
        //dive one more level in
        Math.max.apply(Math, data.map(datum =>
          //get all the keys of the object, throw away "year", then find the max associated values
          Math.max.apply(Math, Object.keys(datum).filter(key => "year" != key).map(key => datum[key] || 0))
        ))
      ));
      return {
        [horizontal ? 'xAxisRange' : 'yAxisRange']: [0, maxValue],
        data: arrOfData
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

var getCostEffectiveness = mkDataGetter({
  path: "costEffectiveness",
  getFillerDatum(year){
    return {
      year: year,
      tender: 0,
      diff: 0
    }
  }
});

var getBidPeriod = mkDataGetter({
  path: "bidPeriod",
  horizontal: true
});

var getTender = [
    ['globalState', 'compareBy'],
    getCostEffectiveness,
    getBidPeriod,
    (compare, costEffectiveness, bidPeriod) => {
      return {
        compare: compare,
        costEffectiveness: costEffectiveness,
        bidPeriod: bidPeriod
      }
    }
];

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