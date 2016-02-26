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

    fetchJson('/api/plannedFundingByLocation/').then(data => dispatcher.dispatch(constants.LOCATION_UPDATED, data));

    Promise.all([
      fetchJson('/api/costEffectivenessTenderAmount/'),
      fetchJson('/api/costEffectivenessAwardAmount/')
    ]).then(([tenderResponse, awardResponse]) => {
      var response2obj = (field, arr) => arr.reduce((obj, elem) => {
        obj[elem._id] = elem[field];
        return obj;
      }, {});

      var tender = response2obj('totalTenderAmount', tenderResponse);
      var award = response2obj('totalAwardAmount', awardResponse);
      dispatcher.dispatch(constants.COST_EFFECTIVENESS_DATA_UPDATED,
          Object.keys(tender).map(year => ({
            year: year,
            tender: tender[year],
            diff: tender[year] - award[year]
          }))
      );
    });

    fetchJson('/api/tenderPriceByVnTypeYear').then(data => dispatcher.dispatch(constants.BID_TYPE_DATA_UPDATED, data));

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

    fetchJson('/api/totalCancelledTendersByYear')
        .then(data => dispatcher.dispatch(constants.CANCELLED_DATA_UPDATED, data))

    fetchJson('/api/ocds/bidTypes').then(data => dispatcher.dispatch(constants.FILTERS_DATA_UPDATED, {
      bidTypes: {
        open: true,
        options: data.reduce((accum, bidType) => {
          var {id} = bidType;
          accum[id] = {
            id: id,
            description: bidType.description,
            selected: false
          };
          return accum;
        }, {})
      }
    }));
  },

  toggleFiltersBox(open){
    dispatcher.dispatch(constants.FILTER_BOX_TOGGLED, open);
  },

  toggleFilter(slug, open){
    dispatcher.dispatch(constants.FILTER_TOGGLED, {
      slug: slug,
      open: open
    })
  },

  toggleFilterOption(slug, option, selected){
    dispatcher.dispatch(constants.FILTER_OPTIONS_TOGGLED, {
      slug: slug,
      option: option,
      selected: selected
    })
  }
}