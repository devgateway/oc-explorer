import {Store, toImmutable} from "nuclear-js";
import constants from "../actions/constants";
import keyMirror from "keymirror";

var store = Store({
  getInitialState(){
    return toImmutable({
      tab: store.tabs.PLANNING,
      year: 2015
    })
  },

  initialize(){
    this.on(constants.CHANGE_TAB, (state, tab) => state.set('tab', tab));
    this.on(constants.CHANGE_YEAR, (state, newYear) => state.set('year', newYear));
  }
});

store.tabs = keyMirror({
  PLANNING: null,
  TENDER_AWARD: null
});

export default store;