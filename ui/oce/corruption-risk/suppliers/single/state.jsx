import { Set } from 'immutable';
import URI from 'urijs';
import { CRD, datefulFilters, indicatorIdsFlat, indicatorTypesMapping, API_ROOT } from '../../../state/oce-state';
import { fetchEP } from '../../../tools';

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

export const flaggedNrData = SupplierState.mapping({
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
      PEName: procuringEntityName,
      wins: count,
      flags: countFlags
    }));
  }
});
