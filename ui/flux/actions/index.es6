import dispatcher from "../dispatcher";
import constants from "./constants";
import {fetchJson, years, identity} from "../../tools";
import URI from "urijs";
import {toImmutable} from "nuclear-js";
import * as endpoints from "./endpoints";

var options2arr = options => Object.keys(options)
    .filter(key => options[key].selected)
    .map(identity);

var addFilters = (filters, url) => null === filters ? url :
    new URI(url).addSearch({
      bidTypeId: options2arr(filters.bidTypes.options),
      bidSelectionMethod: options2arr(filters.bidSelectionMethods.options),
      procuringEntityId: options2arr(filters.procuringEntities.options),
      year: Object.keys(filters.years).filter(year => filters.years[year])
    }).toString();

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

  loadServerSideYearFilteredData(filters = null){
    var load = url => fetchJson(addFilters(filters, url));
    load('/api/topTenLargestTenders').then(data => dispatcher.dispatch(constants.TOP_TENDERS_DATA_UPDATED, data));
    load('/api/topTenLargestAwards').then(data => dispatcher.dispatch(constants.TOP_AWARDS_DATA_UPDATED, data));
  },

  loadData(filters = null){
    var load = url => fetchJson(addFilters(filters, url));
    this.loadServerSideYearFilteredData(filters);
    Promise.all([
      load(endpoints.COUNT_BID_PLANTS_BY_YEAR),
      load(endpoints.COUNT_TENDERS_BY_YEAR),
      load(endpoints.COUNT_AWARDS_BY_YEAR)
    ]).then(([bidplans, tenders, awards]) => dispatcher.dispatch(constants.OVERVIEW_DATA_UPDATED, {
      bidplans: bidplans,
      tenders: tenders,
      awards: awards
    }));

    load(endpoints.PLANNED_FUNDING_BY_LOCATION).then(data => dispatcher.dispatch(constants.LOCATION_UPDATED, data));

    Promise.all([
      load(endpoints.COST_EFFECTIVENESS_TENDER_AMOUNT),
      load(endpoints.COST_EFFECTIVENESS_AWARD_AMOUNT)
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

    load(endpoints.TENDER_PRICE_BY_VN_TYPE_YEAR).then(data => dispatcher.dispatch(constants.BID_TYPE_DATA_UPDATED, data));

    Promise.all([
        load(endpoints.AVERAGE_TENDER_PERIOD),
        load(endpoints.AVERAGE_AWARD_PERIOD)
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

    load(endpoints.TOTAL_CANCELLED_TENDERS_BY_YEAR)
        .then(data => dispatcher.dispatch(constants.CANCELLED_DATA_UPDATED, data));
  },

  bootstrap(){
    this.loadData();
    Promise.all([
      fetchJson('/api/ocds/bidType/all'),
      fetchJson('/api/ocds/bidSelectionMethod/all')
    ]).then(([bidTypes, bidSelectionMethods]) => dispatcher.dispatch(constants.FILTERS_DATA_UPDATED, {
      years: years().reduce((map, year) => map.set(year, true), toImmutable({})),
      bidTypes: {
        open: true,
        options: bidTypes.reduce((accum, bidType) => {
          var {id} = bidType;
          accum[id] = {
            id: id,
            description: bidType.description,
            selected: false
          };
          return accum;
        }, {})
      },
      bidSelectionMethods: {
        open: true,
        options: bidSelectionMethods
            .filter(method => !!method._id)
            .reduce((accum, {_id}) => {
              accum[_id] = {
                id: _id,
                description: _id,
                selected: false
              };
              return accum;
            }, {})
      },
      procuringEntities: {
        open: true,
        options: []
      }
    }));
  },

  setFiltersBox(slug){
    dispatcher.dispatch(constants.FILTER_BOX_CHANGED, slug);
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
  },

  updateProcuringEntityQuery(newQuery){
    dispatcher.dispatch(constants.PROCURING_ENTITY_QUERY_UPDATED, newQuery);
    if (newQuery.length >= 3) {
      fetchJson(new URI('/api/ocds/organization/procuringEntity/all').addSearch('text', newQuery).toString())
          .then(data => {
            dispatcher.dispatch(constants.PROCURING_ENTITIES_UPDATED, data.reduce((accum, procuringEntity) => {
              var {id} = procuringEntity;
              accum[id] = procuringEntity;
              return accum;
            }, {}))
          });
    }
  },

  updateComparisonCriteria(criteria){
    dispatcher.dispatch(constants.COMPARISON_CRITERIA_UPDATED, criteria);
  }
}