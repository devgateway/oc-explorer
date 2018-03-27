import { SupplierState, supplierFilters } from '../state';
import { API_ROOT } from '../../../../state/oce-state';

export const SupplierTableState = SupplierState.substate({
  name: 'SupplierTableState',
});

const supplierProcurementsEP = SupplierTableState.input({
  name: 'supplierProcurementsEP',
  initial: `${API_ROOT}/flaggedRelease/all`,
});

export const page = SupplierTableState.input({
  name: 'page',
  initial: 1,
});

export const pageSize = SupplierTableState.input({
  name: 'pageSize',
  initial: 20,
});

const filters = SupplierTableState.mapping({
  name: 'filters',
  deps: [supplierFilters, page, pageSize],
  mapper: (supplierFilters, page, pageSize) =>
    supplierFilters
      .set('pageSize', pageSize)
      .set('pageNumber', page - 1)
});

const supplierProcurementsRaw = SupplierTableState.remote({
  name: 'supplierProcurementsRaw',
  url: supplierProcurementsEP,
  params: filters,
});

const findActiveAward = awards =>
  awards.find(
    award => award.status === 'active'
  );

function getAwardAmount(awards) {
  const award = findActiveAward(awards);
  const { value } = award;
  return `${value.amount} ${value.currency}`;
}

function getAwardDate(awards) {
  const award = findActiveAward(awards);
  return award.date;
}

export const supplierProcurementsData = SupplierTableState.mapping({
  name: 'supplierProcurementsData',
  deps: [supplierProcurementsRaw],
  mapper: supplierProcurementsRaw =>
    supplierProcurementsRaw.map(datum => {
      return {
        id: datum.id,
        PEName: datum.tender.procuringEntity.name,
        PEId: datum.tender.procuringEntity.id,
        awardAmount: getAwardAmount(datum.awards),
        awardDate: getAwardDate(datum.awards),
        nrBidders: datum.tender.numberOfTenderers,
        types: datum.flags.flaggedStats,
        flags: Object.keys(datum.flags).filter(key => datum.flags[key].value),
      }
    }),
});

const supplierProcurementsCountEP = SupplierTableState.input({
  name: 'supplierProcurementsCountEP',
  initial: `${API_ROOT}/flaggedRelease/count`,
});

export const supplierProcurementsCount = SupplierTableState.remote({
  name: 'supplierProcurementsCount',
  url: supplierProcurementsCountEP,
  params: supplierFilters,
});
