import dispatcher from "../dispatcher";
import constants from "./constants";

export default {
  changeTab(slug){
    dispatcher.dispatch(constants.TAB_CHANGED, slug);
  },

  changeYear(newYear){
    dispatcher.dispatch(constants.YEAR_CHANGED, newYear);
  },

  changeContentWidth(newWidth){
    dispatcher.dispatch(constants.CONTENT_WIDTH_CHANGED, newWidth);
  }
}