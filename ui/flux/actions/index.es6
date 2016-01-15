import dispatcher from "../dispatcher";
import constants from "./constants";

export default {
  changeTab(slug){
    dispatcher.dispatch(constants.CHANGE_TAB, slug);
  },

  changeYear(newYear){
    dispatcher.dispatch(constants.CHANGE_YEAR, newYear);
  }
}