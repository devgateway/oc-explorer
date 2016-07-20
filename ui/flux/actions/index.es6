import dispatcher from "../dispatcher";
import constants from "./constants";
import {fetchJson, pluck, years, identity} from "../../tools";
import URI from "urijs";
import {toImmutable} from "nuclear-js";
import * as endpoints from "./endpoints";

let options2arr = options => Object.keys(options)
    .filter(key => options[key].selected)
    .map(identity);

let addFilters = (filters, url) => null === filters ? url :
    new URI(url).addSearch({
      bidTypeId: options2arr(filters.bidTypes.options),
      bidSelectionMethod: options2arr(filters.bidSelectionMethods.options),
      procuringEntityId: options2arr(filters.procuringEntities.options),
      year: Object.keys(filters.years).filter(year => filters.years[year])
    }).toString();

//Given [[1,2,3], ['a','b','c']] will produce [[1, 'a'], [2, 'b'], [3, 'c']]
let transpose = ([head, ...tail]) => head.map((el, index) => [el].concat(tail.map(pluck(index))));

export default {
  changeContentWidth(newWidth){
    dispatcher.dispatch(constants.CONTENT_WIDTH_CHANGED, newWidth);
  },

  loadData(filters = null){
    let load = url => fetchJson(addFilters(filters, url));
    load(endpoints.TOTAL_CANCELLED_TENDERS_BY_YEAR)
        .then(transformCancelledData)
        .then(data => dispatcher.dispatch(constants.CANCELLED_DATA_UPDATED, data));


    load(endpoints.PERCENT_TENDERS_CANCELLED).then(data => dispatcher.dispatch(constants.CANCELLED_PERCENTS_DATA_UPDATED, data));
  },

  bootstrap(){
    this.loadData();

  },

  setMenuBox(slug){
    dispatcher.dispatch(constants.MENU_BOX_CHANGED, slug);
  },

  toggleFilterOption(slug, option, selected){
    dispatcher.dispatch(constants.FILTER_OPTIONS_TOGGLED, {slug, option, selected});
  },

  updateProcuringEntityQuery(newQuery){
    dispatcher.dispatch(constants.PROCURING_ENTITY_QUERY_UPDATED, newQuery);
    if (newQuery.length >= 3) {
      fetchJson(new URI('/api/ocds/organization/procuringEntity/all').addSearch('text', newQuery).toString())
          .then(data => {
            dispatcher.dispatch(constants.PROCURING_ENTITIES_UPDATED, data.reduce((accum, procuringEntity) => {
              let {id} = procuringEntity;
              accum[id] = procuringEntity;
              return accum;
            }, {}))
          });
    }
  },

  updateComparisonCriteria(criteria){
    dispatcher.dispatch(constants.COMPARISON_CRITERIA_UPDATED, criteria);
  },
  
  setLocale(loc){
    localStorage.lang = loc;
    dispatcher.dispatch(constants.LOCALE_CHANGED, loc);
  },

  applyFilters(){
    dispatcher.dispatch(constants.FILTERS_APPLIED);
  },

  resetFilters(){
    dispatcher.dispatch(constants.FILTERS_RESET);
  },

  toggleCancelledPercents(bool){
    dispatcher.dispatch(constants.CANCELLED_TYPE_TOGGLED, bool);
  }
}