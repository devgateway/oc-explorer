import {Store, toImmutable} from "nuclear-js";
import constants from "../actions/constants";
import keyMirror from "keymirror";
import {years} from "../../tools";

var store = Store({
  getInitialState(){
    return toImmutable({
      tab: store.tabs.PLANNING,
      year: 2015,
      contentWidth: 0,
      data: {
        costEffectiveness: [],
        bidType: years().reduce((obj, year) => {
          obj[year] = [];
          return obj;
        }, {})
      }
    })
  },

  initialize(){
    this.on(constants.TAB_CHANGED, (state, tab) => state.set('tab', tab));
    this.on(constants.YEAR_CHANGED, (state, newYear) => state.set('year', newYear));
    this.on(constants.CONTENT_WIDTH_CHANGED, (state, newWidth) => state.set('contentWidth', newWidth));
    this.on(constants.COST_EFFECTIVENESS_DATA_UPDATED, (state, data) => state.setIn(['data', 'costEffectiveness'], data));
    this.on(constants.BID_TYPE_DATA_UPDATED, (state, {year, data}) => state.setIn(['data', 'bidType', year], data));
  }
});

store.tabs = keyMirror({
  PLANNING: null,
  TENDER_AWARD: null
});

export default store;