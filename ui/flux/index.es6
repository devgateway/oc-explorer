import dispatcher from "./dispatcher";
import actions from "./actions";
import globalStateStore from "./stores/global-state";
import {toImmutable} from "nuclear-js";
import {Map, List} from "immutable";
import {identity, pluck} from '../tools';

dispatcher.registerStores({
  globalState: globalStateStore
});

var ensureArray = maybeArray => Array.isArray(maybeArray) ? maybeArray : [];

var ensureList = maybeList => maybeList || List();

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

var getCriteriaNames = (compare, comparisonCriteriaNames, filters) => {
  switch(compare){
    case "bidTypeId":
      return ensureArray(comparisonCriteriaNames).map(name =>
          filters.getIn(['bidTypes', 'options']).find(bidType => bidType.get('id') == name[0]).get('description')
      );
    default:
      return ensureArray(comparisonCriteriaNames).map(pluck('_id'));
  }
}

var mkDataGetter = ({path, getFillerDatum = yearOnly, horizontal = false, getMaxField =  maxFieldExceptYear}) => [
  ['globalState', 'compareBy'],
  ['globalState', 'data', path],
  ['globalState', 'comparisonData', path],
  getSelectedYears,
  ['globalState', 'comparisonCriteriaNames'],
  ['globalState', 'filters'],
  (compare, rawData, comparisonData, years, comparisonCriteriaNames, filters) => {
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
        criteriaNames: getCriteriaNames(compare, comparisonCriteriaNames, filters),
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

var getBidType = [
  ['globalState', 'compareBy'],
  ['globalState', 'data', 'bidType'],
  ['globalState', 'comparisonData', 'bidType'],
  ['globalState', 'filters', 'years'],
  ['globalState', 'comparisonCriteriaNames'],
  ['globalState', 'filters'],
  (compare, rawData, comparisonData, rawYears, comparisonCriteriaNames, filters) => {
    var years = ensureList(rawYears);
    var collectCats = arrOfData => arrOfData.reduce((cats, data) => data
      .filter(bidType => years.get(bidType.get('year')), false)
      .groupBy(bidType => bidType.get('procurementMethodDetails'))
      .reduce((cats, bidTypes) => {
        var name = bidTypes.getIn([0, 'procurementMethodDetails']) || 'unspecified';
        return cats.set(name, Map({
          _id: name,
          totalTenderAmount: 0
        }))
      }, cats)
    , Map());

    var parse = cats => data => data
      .reduce((cats, datum) => {
        var name = datum.get('procurementMethodDetails') || 'unspecified';
        var path = [name, 'totalTenderAmount'];
        return cats.setIn(path, cats.getIn(path) + datum.get('totalTenderAmount'));
      }, cats)
      .toList().toJS()

    if (compare) {
      var cats = collectCats(ensureArray(comparisonData));
      var arrOfData = ensureArray(comparisonData).map(parse(cats));
      var maxValue = Math.max.apply(Math, arrOfData.map(data =>
          Math.max.apply(Math, data.map(pluck('totalTenderAmount')))
      ));
      return {
        criteriaNames: getCriteriaNames(compare, comparisonCriteriaNames, filters),
        yAxisRange: [0, maxValue],
        data: arrOfData
      }
    } else {
      var data = ensureList(rawData);
      var cats = collectCats([data]);
      return parse(cats)(data);
    }
  }
];

var getTender = [
    ['globalState', 'compareBy'],
    getCostEffectiveness,
    getBidPeriod,
    getBidType,
    getCancelled,
    (compare, costEffectiveness, bidPeriod, bidType, cancelled) => {
      return {
        compare: compare,
        costEffectiveness: costEffectiveness,
        bidPeriod: bidPeriod,
        bidType: bidType,
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