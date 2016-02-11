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
    Promise.all([
      fetchJson('/api/countBidPlansByYear'),
      fetchJson('/api/countTendersByYear'),
      fetchJson('/api/countAwardsByYear')
    ]).then(([bidplans, tenders, awards]) => dispatcher.dispatch(constants.OVERVIEW_DATA_UPDATED, {
      bidplans: bidplans,
      tenders: tenders,
      awards: awards
    }));

    years().forEach(year => {
      fetchJson(`/api/plannedFundingByLocation/${year}`)
          .then(data => data.filter(location => !!location.coordinates || console.warn('Invalid location!', location)))
          .then(data => dispatcher.dispatch(constants.LOCATION_UPDATED, {year: year, data: data}));

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
      }).then(data => dispatcher.dispatch(constants.COST_EFFECTIVENESS_DATA_UPDATED, {year: year, data: data}));

      fetchJson(`/api/tenderPriceByVnTypeYear/${year}`)
          .then(data => dispatcher.dispatch(constants.BID_TYPE_DATA_UPDATED, {year: year, data: data}))
    });

    fetchJson("/api/tenderBidPeriodPercentiles")
        .then(raw => {
          var parsed = {};
          Object.keys(raw).forEach(key => parsed[key] = raw[key].tenderLengthDays);
          return parsed;
        })
        .then(dispatcher.dispatch.bind(dispatcher, constants.BID_PERIOD_DATA_UPDATED));
  }
}