import { Map, Set } from 'immutable';
import State from './index';
const API_ROOT = '/api';

export const OCE = new State({ name: 'oce' });
export const CRD = OCE.substate({ name: 'crd' });

export const filters = CRD.input({
  name: 'filters',
  initial: Map(),
});

CRD.mapping({
  name: 'datelessFilters',
  deps: [filters],
  mapper: filters => filters.delete('years').delete('months'),
});

export const supplierId = CRD.input({
  name: 'supplierId'
});

const supplierFilters = CRD.mapping({
  name: 'supplierFilters',
  deps: [filters, supplierId],
  mapper: (filters, supplierId) => 
    filters.update('supplierId', Set(), supplierIds => supplierIds.add(supplierId))
});

const winsAndFlagsRaw = CRD.remote({
  name: 'winsAndFlagsRaw',
  initialUrl: `${API_ROOT}/supplierWinsPerProcuringEntity`,
  paramsMapping: {
    deps: [supplierFilters],
    mapper: filter => filter.toJS()
  }
});

export const winsAndFlagsData = CRD.mapping({
  name: 'winsAndFlagsData',
  deps: [winsAndFlagsRaw.result],
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