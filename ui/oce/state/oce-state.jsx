import { Map, Set } from 'immutable';
import State from './index';
const API_ROOT = '/api';

export const OCE = new State({ name: 'oce' });
export const CRD = OCE.substate({ name: 'crd' });

export const filters = CRD.input({
  name: 'filters',
  initial: Map(),
});

const indicatorTypesMapping = CRD.remote({
  name: 'indicatorTypesMapping'
});

indicatorTypesMapping.input({
  name: 'url',
  initial: `${API_ROOT}/indicatorTypesMapping`
})


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

export const supplierId = CRD.input({
  name: 'supplierId'
});

const supplierFilters = CRD.mapping({
  name: 'supplierFilters',
  deps: [datefulFilters, supplierId],
  mapper: (filters, supplierId) => 
    filters.update('supplierId', Set(), supplierIds => supplierIds.add(supplierId))
});

const winsAndFlagsRaw = CRD.remote({
  name: 'winsAndFlagsRaw',
});

winsAndFlagsRaw.mapping({
  name: 'params',
  deps: [supplierFilters],
  mapper: filter => filter.toJS()
});

winsAndFlagsRaw.input({
  name: 'url',
  initial: `${API_ROOT}/supplierWinsPerProcuringEntity`
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
