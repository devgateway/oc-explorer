import {Store, toImmutable} from "nuclear-js";
import constants from "../actions/constants";
import keyMirror from "keymirror";
import {identity} from "../../tools";
import actions from "../actions";

let mapIn = (obj, path, cb) => obj.updateIn(path, items => items.map(cb));

export let tabs = keyMirror({
  OVERVIEW: null,
  PLANNING: null,
  TENDER_AWARD: null
});

let store = Store({
  getInitialState(){
    return toImmutable({
      filtersBox: false,
      compareBy: "",
      tab: tabs.OVERVIEW,
      contentWidth: 0,
      data: {},
      comparisonData: {},
      procuringEntityQuery: "",
      filters: {},
      locale: localStorage.lang || 'en',
      showPercentsCancelled: false
    })
  },

  initialize(){
    let updateData = (path, pipe = identity) => (state, data) => state.setIn(['data', path], pipe(data));

    this.on(constants.TAB_CHANGED, (state, tab) => state.set('tab', tab));
    this.on(constants.YEAR_TOGGLED, (state, {year, selected}) => {
      let newState = state.setIn(['filters', 'years', year], selected);
      actions.loadServerSideYearFilteredData(newState.get('filters').toJS());
      return newState;
    });
    this.on(constants.CONTENT_WIDTH_CHANGED, (state, newWidth) => state.set('contentWidth', newWidth));
    this.on(constants.COST_EFFECTIVENESS_DATA_UPDATED, updateData('costEffectiveness'));
    this.on(constants.BID_TYPE_DATA_UPDATED, updateData('bidType', toImmutable));
    this.on(constants.LOCATION_UPDATED, updateData('locations', toImmutable));
    this.on(constants.BID_PERIOD_DATA_UPDATED, updateData('bidPeriod'));
    this.on(constants.OVERVIEW_DATA_UPDATED, updateData('overview'));
    this.on(constants.CANCELLED_DATA_UPDATED, updateData('cancelled'));
    this.on(constants.CANCELLED_PERCENTS_DATA_UPDATED, updateData('cancelledPercents'));
    this.on(constants.TOP_TENDERS_DATA_UPDATED, updateData('topTenders'));
    this.on(constants.TOP_AWARDS_DATA_UPDATED, updateData('topAwards'));
    this.on(constants.AVERAGED_TENDERS_DATA_UPDATED, updateData('avgTenders'));
    this.on(constants.FILTER_BOX_CHANGED, (state, slug) => state.set('filtersBox', slug));
    this.on(constants.FILTERS_DATA_UPDATED, (state, data) => state.set('filters', toImmutable(data)));
    this.on(constants.FILTER_TOGGLED, (state, {slug, open}) => state.setIn(['filters', slug, 'open'], open));
    this.on(constants.FILTER_OPTIONS_TOGGLED, (state, {slug, option, selected}) =>
      state.setIn(['filters', slug, 'options', option, 'selected'], selected)
    );

    this.on(constants.FILTERS_APPLIED, state => (actions.loadData(state.get('filters').toJS()), state));

    this.on(constants.FILTERS_RESET, state =>
        mapIn(state, ['filters'], filter =>
          filter.has('options') ?
              mapIn(filter, ['options'], option => option.set('selected', false)) :
              filter
        )
    );

    this.on(constants.PROCURING_ENTITY_QUERY_UPDATED, (state, newQuery) => state.set('procuringEntityQuery', newQuery));
    this.on(constants.PROCURING_ENTITIES_UPDATED, (state, procuringEntities) =>
        state.setIn(['filters', 'procuringEntities', 'options'], toImmutable(procuringEntities)));
    this.on(constants.COMPARISON_CRITERIA_UPDATED, (state, criteria) => {
      let newState = state.set('compareBy', criteria);
      actions.loadComparisonData(newState.get('compareBy'), newState.get('filters').toJS());
      return newState;
    });
    this.on(constants.OVERVIEW_COMPARISON_DATA_UPDATED, (state, data) => state.setIn(['comparisonData', 'overview'], data));
    this.on(constants.COST_EFFECTIVENESS_COMPARISON_DATA_UPDATED, (state, data) =>
        state.setIn(['comparisonData', 'costEffectiveness'], data));
    this.on(constants.BID_PERIOD_COMPARISON_DATA_UPDATED, (state, data) =>
        state.setIn(['comparisonData', 'bidPeriod'], data));
    this.on(constants.BID_TYPE_COMPARISON_DATA_UPDATED, (state, data) =>
        state.setIn(['comparisonData', 'bidType'], data.map(toImmutable)));
    this.on(constants.CANCELLED_COMPARISON_DATA_UPDATED, (state, data) =>
        state.setIn(['comparisonData', 'cancelled'], data));
    this.on(constants.COMPARISON_CRITERIA_NAMES_UPDATED, (state, names) => state.set('comparisonCriteriaNames', names))
    
    this.on(constants.LOCALE_CHANGED, (state, loc) => state.set('locale', loc));

    this.on(constants.CANCELLED_TYPE_TOGGLED, (state, percents) => state.set('showPercentsCancelled', percents));
  }
});

export default store;