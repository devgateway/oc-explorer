import dispatcher from "./dispatcher";
import actions from "./actions";
import globalStateStore from "./stores/global-state";
import {toImmutable} from "nuclear-js";
import {Map, List} from "immutable";
import {identity, pluck} from '../tools';

dispatcher.registerStores({
  globalState: globalStateStore
});

let ensureArray = maybeArray => Array.isArray(maybeArray) ? maybeArray : [];

let ensureList = maybeList => maybeList || List();

let obj2arr = obj => Object.keys(obj).map(key => obj[key]);

var getSelectedYears = [
  ['globalState', 'filters', 'years'],
  cacheFn(
    years => years ? years.filter(identity).keySeq().toArray() : []
  )
];


const FALLBACK_BIDTYPE = Map({description: "Other"});

let getCriteriaNames = (compare, comparisonCriteriaNames, filters) => {
  switch(compare){
    case "bidTypeId":
      return ensureArray(comparisonCriteriaNames).map(name =>
          filters.getIn(['bidTypes', 'options'])
              .find(bidType => bidType.get('id') == name[0], undefined, FALLBACK_BIDTYPE).get('description')
      );
    default:
      return ensureArray(comparisonCriteriaNames).map(pluck('_id'));
  }
}

let getBidType = [
  ['globalState', 'compareBy'],
  ['globalState', 'data', 'bidType'],
  ['globalState', 'comparisonData', 'bidType'],
  ['globalState', 'filters', 'years'],
  ['globalState', 'comparisonCriteriaNames'],
  ['globalState', 'filters'],
  cacheFn(
    (compare, rawData, comparisonData, rawYears, comparisonCriteriaNames, filters) => {
      var years = ensureList(rawYears);


    let parse = cats => data => data
      .reduce((cats, datum) => {
        let name = datum.get('procurementMethodDetails') || 'unspecified';
        let path = [name, 'totalTenderAmount'];
        return cats.setIn(path, cats.getIn(path) + datum.get('totalTenderAmount'));
      }, cats)
      .toList().toJS()

    if (compare) {
      let cats = collectCats(ensureArray(comparisonData));
      let arrOfData = ensureArray(comparisonData).map(parse(cats));
      let maxValue = Math.max.apply(Math, arrOfData.map(data =>
          Math.max.apply(Math, data.map(pluck('totalTenderAmount')))
      ));
      return {
        criteriaNames: getCriteriaNames(compare, comparisonCriteriaNames, filters),
        yAxisRange: [0, maxValue],
        data: arrOfData
      }
    } else {
      let data = ensureList(rawData);
      let cats = collectCats([data]);
      return parse(cats)(data);
    }
  }
  )
];

let getAvgTenders = mkDataGetter({
  path: "avgTenders"
});

let getCompetitiveness = [
  ['globalState', 'compareBy'],
  getAvgTenders,
  getBidType,
  getCostEffectiveness,
  (compare, avgNrBids, bidType, costEffectiveness) => ({compare, avgNrBids, bidType, costEffectiveness})
];

let getEfficiency = [
  ['globalState', 'compareBy'],
  getBidPeriod,
  getCancelled,
  getCancelledPercents,
  ['globalState', 'showPercentsCancelled'],
  (compare, bidPeriod, cancelled, cancelledPercents, showPercentsCancelled) => ({
    compare,
    bidPeriod,
    showPercentsCancelled,
    cancelled: showPercentsCancelled ? cancelledPercents : cancelled,
  })
];

let getPercentEbid = mkDataGetter({
  path: "percentEbid"
});

let getPercentEprocurement = mkDataGetter({
  path: "percentEprocurement"
});

let getEProcurement = [
  ['globalState', 'compareBy'],
  getPercentEbid,
  getPercentEprocurement,
  (compare, percentEbid, percentEprocurement) => ({compare, percentEbid, percentEprocurement})
];

let getGlobalState = [
    [],
    state => state
        .set('overview', dispatcher.evaluate(getOverview))
        .set('competitiveness', dispatcher.evaluate(getCompetitiveness))
        .set('efficiency', dispatcher.evaluate(getEfficiency))
        .set('eProcurement', dispatcher.evaluate(getEProcurement))
];

export default {
  actions: actions,
  onUpdate(cb, trigger = false){
    dispatcher.observe(getGlobalState, cb);
    if(trigger) cb(dispatcher.evaluate(getGlobalState));
  }
}