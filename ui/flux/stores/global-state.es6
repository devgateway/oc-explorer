import {Store, toImmutable} from "nuclear-js";
import constants from "../actions/constants";
import keyMirror from "keymirror";

var store = Store({
  getInitialState(){
    return toImmutable({
      tab: store.tabs.PLANNING,
      year: 2015,
      contentWidth: 0
    })
  },

  initialize(){
    this.on(constants.TAB_CHANGED, (state, tab) => state.set('tab', tab));
    this.on(constants.YEAR_CHANGED, (state, newYear) => state.set('year', newYear));
    this.on(constants.CONTENT_WIDTH_CHANGED, (state, newWidth) => state.set('contentWidth', newWidth));
  }
});

store.tabs = keyMirror({
  PLANNING: null,
  TENDER_AWARD: null
});

export default store;