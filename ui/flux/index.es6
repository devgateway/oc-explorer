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

var maxFieldExceptYear = datum =>
    Math.max.apply(Math, Object.keys(datum).filter(key => "year" != key).map(key => datum[key] || 0));

var mkDataGetter = ({path, getFillerDatum = yearOnly, horizontal = false, getMaxField =  maxFieldExceptYear}) => [
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
      var maxValue = Math.max.apply(Math, arrOfData.map(data =>
        Math.max.apply(Math, data.map(getMaxField))
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
  },
  getMaxField({tender, diff}){
    return tender + diff;
  }
});

var getBidPeriod = mkDataGetter({
  path: "bidPeriod",
  horizontal: true,
  getMaxField({tender, award}){
    return tender + award;
  }
});

var getCancelled = mkDataGetter({
  path: "cancelled"
});

var getTender = [
    ['globalState', 'compareBy'],
    getCostEffectiveness,
    getBidPeriod,
    getCancelled,
    (compare, costEffectiveness, bidPeriod, cancelled) => {
      return {
        compare: compare,
        costEffectiveness: costEffectiveness,
        bidPeriod: bidPeriod,
        cancelled: cancelled
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