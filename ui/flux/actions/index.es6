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

    Promise.all([
        fetchJson('/api/averageTenderPeriod'),
        fetchJson('/api/averageAwardPeriod')
    ]).then(([tenders, awards]) => {
      var awardsHash = awards.reduce((obj, award) => {
        obj[award._id] = award.averageAwardDays
        return obj;
      }, {});
      return tenders.map(tender => ({
          year: tender._id,
          tender: tender.averageTenderDays,
          award: awardsHash[tender._id] || 0
        })
      )
    }).then(data => dispatcher.dispatch(constants.BID_PERIOD_DATA_UPDATED, data));

    Promise.resolve(
        [{"_id":2010,"totalCancelledTendersAmount":4.401766E9},{"_id":2011,"totalCancelledTendersAmount":3.0435778033E10},{"_id":2012,"totalCancelledTendersAmount":2.34826483068E11},{"_id":2013,"totalCancelledTendersAmount":1.30228748521E11},{"_id":2014,"totalCancelledTendersAmount":2.67683186352E11},{"_id":2015,"totalCancelledTendersAmount":2.688935000512E12}]
    ).then(data => dispatcher.dispatch(constants.CANCELLED_DATA_UPDATED, data))
  }
}