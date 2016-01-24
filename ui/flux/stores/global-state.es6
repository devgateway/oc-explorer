import {Store, toImmutable} from "nuclear-js";
import constants from "../actions/constants";
import keyMirror from "keymirror";
import {years, identity} from "../../tools";

var store = Store({
  getInitialState(){
    return toImmutable({
      tab: store.tabs.PLANNING,
      selectedYears: years().reduce((map, year) => map.set(year, true), toImmutable({})),
      contentWidth: 0,
      data: {}
    })
  },

  initialize(){
    var updateData = (path, pipe = identity) => (state, {year, data}) => state.setIn(['data', path, year], pipe(data));
    this.on(constants.TAB_CHANGED, (state, tab) => state.set('tab', tab));
    this.on(constants.YEAR_TOGGLED, (state, {year, selected}) => state.setIn(['selectedYears', year], selected));
    this.on(constants.CONTENT_WIDTH_CHANGED, (state, newWidth) => state.set('contentWidth', newWidth));
    this.on(constants.COST_EFFECTIVENESS_DATA_UPDATED, updateData('costEffectiveness'));
    this.on(constants.BID_TYPE_DATA_UPDATED, updateData('bidType', toImmutable));
    this.on(constants.LOCATION_UPDATED, updateData('locations', toImmutable));
    this.on(constants.BID_PERIOD_DATA_UPDATED, (state, data) => state.setIn(['data', 'bidPeriod'], data));
  }
});

store.tabs = keyMirror({
  PLANNING: null,
  TENDER_AWARD: null
});

export default store;