import dispatcher from "../dispatcher";
import constants from "./constants";
import {fetchJson, years} from "../../tools";

export default {
  changeTab(slug){
    dispatcher.dispatch(constants.TAB_CHANGED, slug);
  },

  toggleYear(year, selected){
    dispatcher.dispatch(constants.YEAR_TOGGLED, {
      year: year,
      selected: selected
    });
  },

  changeContentWidth(newWidth){
    dispatcher.dispatch(constants.CONTENT_WIDTH_CHANGED, newWidth);
  },

  loadData(){
    years().forEach(
        year => fetchJson(`/api/plannedFundingByLocation/${year}`)
          .then(data => data.filter(location => !!location.coordinates
              || console.warn('Invalid location!', location)))
          .then(data => dispatcher.dispatch(constants.LOCATION_UPDATED, {year: year, data: data}))
    );

    Promise.all(years().map(year =>
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

    years().forEach(
        year => fetchJson(`/api/tenderPriceByOcdsTypeYear/${year}`)
          .then(data => dispatcher.dispatch(constants.BID_TYPE_DATA_UPDATED, {year: year, data: data}))
    )
  }
}