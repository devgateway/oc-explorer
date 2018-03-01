import { Map, Set } from 'immutable';
import URI from 'urijs';
import { fetchEP } from '../tools';
import State from './index';
const API_ROOT = '/api';

export const OCE = new State({ name: 'oce' });
export const CRD = OCE.substate({ name: 'crd' });

export const filters = CRD.input({
  name: 'filters',
  initial: Map(),
});

export const supplierId = CRD.input({
  name: 'supplierId'
});

const datelessFilters = CRD.mapping({
  name: 'datelessFilters',
  deps: [filters],
  mapper: filters => filters.delete('years').delete('months'),
});

const datefulFilters = CRD.mapping({
  name: 'datefulFilters',
  deps: [datelessFilters, filters],
  mapper: (datelessFilters, filters) =>
    datelessFilters.set('year', filters.get('years'))
      .set('month', filters.get('months'))
});

const supplierFilters = CRD.mapping({
  name: 'supplierFilters',
  deps: [datefulFilters, supplierId],
  mapper: (filters, supplierId) => 
    filters.update('supplierId', Set(), supplierIds => supplierIds.add(supplierId))
});

const indicatorTypesMappingURL = CRD.input({
  name: 'indicatorTypesMappingURL',
  initial: `${API_ROOT}/indicatorTypesMapping`,
})

const indicatorTypesMapping = CRD.remote({
  name: 'indicatorTypesMapping',
  url: indicatorTypesMappingURL
});

const indicatorIdsFlat = CRD.mapping({
  name: 'indicatorIdsFlat',
  deps: [indicatorTypesMapping],
  mapper: indicatorTypesMapping => Object.keys(indicatorTypesMapping),
});

export const flaggedNrData = CRD.mapping({
  name: 'flaggedNrData',
  deps: [indicatorIdsFlat, supplierFilters, indicatorTypesMapping],
  mapper: (indicatorIds, filters, indicatorTypesMapping) => Promise.all(
    indicatorIds.map(indicatorId =>
      fetchEP(
        new URI(`${API_ROOT}/flags/${indicatorId}/count`).addSearch(filters.toJS())
      ).then(data => {
        if (!data[0]) return null;
        return {
          indicatorId,
          count: data[0].count,
          types: indicatorTypesMapping[indicatorId].types
        }
      })
    )
  ).then(data =>
    data.filter(datum => !!datum).sort((a, b) => b.count - a.count)
  )
})

const winsAndFlagsURL = CRD.input({
  name: 'winsAndFlagsURL',
  initial: `${API_ROOT}/supplierWinsPerProcuringEntity`
})

const winsAndFlagsRaw = CRD.remote({
  name: 'winsAndFlagsRaw',
  url: winsAndFlagsURL,
  params: supplierFilters
});

export const winsAndFlagsData = CRD.mapping({
  name: 'winsAndFlagsData',
  deps: [winsAndFlagsRaw],
  mapper(raw) {
    return raw.map(({ count, countFlags, procuringEntityName}) => ({
      PEName: procuringEntityName,
      wins: count,
      flags: countFlags
    }));
  }
})

/* const supplierDetailsURL = CRD.map({
 *   name: 'supplierDetailsURL',
 *   deps: [supplierId],
 *   mapper: id => `${API_ROOT}/ocds/organization/supplier/id/${id}`
 * });
 *  
 * CRD.endpoint({
 *   name: 'supplierDetails',
 *   url: supplierDetailsURL
 * });*/

/* state.input({
 *   name: 'totalFlagsURL',
 *   initial: `${API_ROOT}/totalFlags`
 * })
 * 
 * state.endpoint({
 *   name: 'supplierTotalFlags',
 *   url: 'totalFlagsURL',
 *   params: 'supplierFilters'
 * });
 * 
 * state.input({
 *   name: 'releaseCountURL',
 *   initial: `${API_ROOT}/ocds/release/count`
 * });
 * 
 * state.endpoint({
 *   name: 'supplierReleasesCount',
 *   url: 'releaseCountURL',
 *   params: 'supplierFilters'
 * });*/

export default CRD;
