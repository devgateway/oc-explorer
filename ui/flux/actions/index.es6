import dispatcher from "../dispatcher";
import constants from "./constants";
import {fetchJson} from "../../tools";

export default {
  changeTab(slug){
    dispatcher.dispatch(constants.TAB_CHANGED, slug);
  },

  changeYear(newYear){
    dispatcher.dispatch(constants.YEAR_CHANGED, newYear);
  },

  changeContentWidth(newWidth){
    dispatcher.dispatch(constants.CONTENT_WIDTH_CHANGED, newWidth);
  },

  loadData(){
    Promise.all([2011, 2012, 2013, 2014, 2015].map(year =>
      Promise.all([
        fetchJson(`/api/costEffectivenessTenderAmount/${year}`),
        fetchJson(`/api/costEffectivenessAwardAmount/${year}`)
      ]).then(([tenderResponse, awardResponse]) => {
        var tender = tenderResponse[0].totalTenderAmount;
        var award = awardResponse[0].totalAwardAmount;
        return {
          year: year,
          tender: tender,
          diff: tender - award
        }
      })
    )).then(dispatcher.dispatch.bind(dispatcher, constants.COST_EFFECTIVENESS_DATA_UPDATED));
  }
}