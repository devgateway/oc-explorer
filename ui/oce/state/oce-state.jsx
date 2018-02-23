import { Map, Set } from 'immutable';
import State from './index';
const API_ROOT = '/api';

const state = new State();

state.input({
  name: 'filters',
  initial: Map()
});

state.map({
  name: 'datelessFilters',
  deps: ['filters'],
  mapper: filters => filters.delete('years').delete('months')
});

state.input({
  name: 'supplierId'
});

state.map({
  name: 'supplierFilters',
  deps: ['filters', 'supplierId'],
  mapper: (filters, supplierId) => 
    filters.update('supplierId', Set(), supplierIds => supplierIds.add(supplierId))
});

state.input({
  name: 'winsAndFlagsURL',
  initial: `${API_ROOT}/supplierWinsPerProcuringEntity`
});

state.endpoint({
  name: 'winsAndFlagsRaw',
  url: 'winsAndFlagsURL',
  params: 'supplierFilters'
});

state.map({
  name: 'winsAndFlagsData',
  deps: ['winsAndFlagsRaw'],
  mapper(raw) {
    return raw.map(({ count, countFlags, procuringEntityName}) => ({
      PEName: procuringEntityName,
      wins: count,
      flags: countFlags
    }));
  }
})

/* state.map({
 *   name: 'supplierDetailsURL',
 *   deps: ['supplierId'],
 *   mapper: id => `${API_ROOT}/ocds/organization/supplier/id/${id}`
 * });
 * 
 * state.endpoint({
 *   name: 'supplierDetails',
 *   url: 'supplierDetailsURL'
 * });
 * 
 * state.input({
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

export default state;