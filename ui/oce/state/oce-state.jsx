import { Map, Set } from 'immutable';
import URI from 'urijs';
import { fetchEP } from '../tools';
import State from './index';
export const API_ROOT = '/api';

export const OCE = new State({ name: 'oce' });
export const CRD = OCE.substate({ name: 'crd' });

export const filters = CRD.input({
  name: 'filters',
  initial: Map(),
});

const datelessFilters = CRD.mapping({
  name: 'datelessFilters',
  deps: [filters],
  mapper: filters => filters.delete('years').delete('months'),
});

export const datefulFilters = CRD.mapping({
  name: 'datefulFilters',
  deps: [datelessFilters, filters],
  mapper: (datelessFilters, filters) =>
    datelessFilters.set('year', filters.get('years'))
      .set('month', filters.get('months'))
});

const indicatorTypesMappingURL = CRD.input({
  name: 'indicatorTypesMappingURL',
  initial: `${API_ROOT}/indicatorTypesMapping`,
})

export const indicatorTypesMapping = CRD.remote({
  name: 'indicatorTypesMapping',
  url: indicatorTypesMappingURL
});

export const indicatorIdsFlat = CRD.mapping({
  name: 'indicatorIdsFlat',
  deps: [indicatorTypesMapping],
  mapper: indicatorTypesMapping => Object.keys(indicatorTypesMapping),
});
