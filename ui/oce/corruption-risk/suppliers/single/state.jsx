import { Set } from 'immutable';
import { CRD, datefulFilters, indicatorIdsFlat, indicatorTypesMapping, API_ROOT } from '../../../state/oce-state';
import { FlaggedNrMapping } from '../../archive/state';

export const SupplierState = CRD.substate({
  name: 'SupplierState'
});

export const supplierId = SupplierState.input({
  name: 'supplierId'
});

export const supplierFilters = SupplierState.mapping({
  name: 'supplierFilters',
  deps: [datefulFilters, supplierId],
  mapper: (filters, supplierId) =>
    filters.update('supplierId', Set(), supplierIds => supplierIds.add(supplierId))
});

export const supplierFlaggedNrData = new FlaggedNrMapping({
  name: 'supplierFlaggedNrData',
  filters: supplierFilters,
  parent: SupplierState,
});

const winsAndFlagsURL = SupplierState.input({
  name: 'winsAndFlagsURL',
  initial: `${API_ROOT}/supplierWinsPerProcuringEntity`
})

const winsAndFlagsRaw = SupplierState.remote({
  name: 'winsAndFlagsRaw',
  url: winsAndFlagsURL,
  params: supplierFilters
});

export const winsAndFlagsData = SupplierState.mapping({
  name: 'winsAndFlagsData',
  deps: [winsAndFlagsRaw],
  mapper(raw) {
    return raw.map(({ count, countFlags, procuringEntityName}) => ({
      name: procuringEntityName,
      wins: count,
      flags: countFlags
    }));
  }
});

export const maxCommonDataLength = SupplierState.mapping({
  name: 'maxCommonDataLength',
  deps: [winsAndFlagsData, supplierFlaggedNrData],
  mapper: (a, b) => Math.min(
    5,
    Math.max(a.length, b.length)
  ),
})
