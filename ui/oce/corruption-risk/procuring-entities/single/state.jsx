import { Set } from 'immutable';
import { CRD, datefulFilters, API_ROOT } from '../../../state/oce-state';
import { FlaggedNrMapping } from '../../archive/state';

export const PEState = CRD.substate({
  name: 'PEState',
});

export const PEId = PEState.input({
  name: 'PEId',
});

export const PEFilters = PEState.mapping({
  name: 'PEFilters',
  deps: [datefulFilters, PEId],
  mapper: (filters, PEId) =>
    filters.update(
      'procuringEntityId',
      Set(),
      PEIds => PEIds.add(PEId)
    )
});

const PEInfoUrl = PEState.mapping({
  name: 'PEInfoUrl',
  deps: [PEId],
  mapper: id => `${API_ROOT}/ocds/organization/procuringEntity/id/${id}`,
});

export const PEInfo = PEState.remote({
  name: 'PEInfoRaw',
  url: PEInfoUrl,
});

const PEFlagsUrl = PEState.input({
  name: 'PEFlagsUrl',
  initial: `${API_ROOT}/totalFlags`,
});

const PEFlagsCountRaw = PEState.remote({
  name: 'PEFlagsCountRaw',
  url: PEFlagsUrl,
  params: PEFilters,
});

export const PEFlagsCount = PEState.mapping({
  name: 'PEFlagsCount',
  deps: [PEFlagsCountRaw],
  mapper: data => data[0].flaggedCount,
});

const contractsUrl = PEState.input({
  name: 'contractsUrl',
  initial: `${API_ROOT}/flaggedRelease/all`
})

const associatedContracts = PEState.remote({
  name: 'associatedContracts',
  url: contractsUrl,
  params: PEFilters,
});

const associatedBuyersURL = PEState.input({
  name: 'associatedBuyersURL',
  initial: `${API_ROOT}/buyersForProcuringEntities`
});

export const associatedBuyers = PEState.remote({
  name: 'associatedBuyers',
  url: associatedBuyersURL,
  params: PEFilters,
});

const contractsCountUrl = PEState.input({
  name: 'contractsCountUrl',
  initial: `${API_ROOT}/flaggedRelease/count`
});

export const associatedContractsCount = PEState.remote({
  name: 'associatedContractsCount',
  url: contractsCountUrl,
  params: PEFilters,
});

const unflaggedContractsCountUrl = PEState.input({
  name: 'unflaggedContractsCountUrl',
  initial: `${API_ROOT}/ocds/release/count`,
});

export const associatedUnflaggedContractsCount = PEState.remote({
  name: 'associatedUnflaggedContractsCount',
  url: unflaggedContractsCountUrl,
  params: PEFilters,
});

const associatedSuppliers = PEState.mapping({
  name: 'associatedSuppliers',
  deps: ['associatedContracts'],
})

export const PEFlaggedNrData = new FlaggedNrMapping({
  name: 'PEFlaggedNrData',
  filters: PEFilters,
  parent: PEState,
});

const winsAndFlagsURL = PEState.input({
  name: 'winsAndFlagsURL',
  initial: `${API_ROOT}/supplierWinsPerProcuringEntity`,
});

const winsAndFlagsRaw = PEState.remote({
  name: 'winsAndFlagsRaw',
  url: winsAndFlagsURL,
  params: PEFilters,
});

export const winsAndFlagsData = PEState.mapping({
  name: 'winsAndFlagsData',
  deps: [winsAndFlagsRaw],
  mapper: data => data.map(datum => {
    return {
      name: datum.supplierName,
      wins: datum.count,
      flags: datum.countFlags,
    }
  })
});

const procurementsByMethodUrl = PEState.input({
  name: 'procurementsByMethodUrl',
  initial: `${API_ROOT}/procurementsByProcurementMethod`,
});

const procurementsByMethodRaw = PEState.remote({
  name: 'procurementsByMethodRaw',
  url: procurementsByMethodUrl,
  params: PEFilters,
});

export const procurementsByMethodData = PEState.mapping({
  name: 'procurementsByMethodData',
  deps: [procurementsByMethodRaw],
  mapper: data => data.map(
    datum => ({
      status: datum.tenderStatus,
      count: datum.count,
    })
  ).sort((a, b) => b.count - a.count)
});

const procurementsByStatusUrl = PEState.input({
  name: 'procurementsByStatusUrl',
  initial: `${API_ROOT}/procurementsByTenderStatus`,
});

const procurementsByStatusRaw = PEState.remote({
  name: 'procurementsByStatusRaw',
  url: procurementsByStatusUrl,
  params: PEFilters,
});

export const procurementsByStatusData = PEState.mapping({
  name: 'procurementsByStatusData',
  deps: [procurementsByStatusRaw],
  mapper: data => data.map(
    datum => ({
      status: datum.tenderStatus,
      count: datum.count,
    })
  ).sort((a, b) => b.count - a.count)
});

export const maxCommonDataLength = PEState.mapping({
  name: 'maxCommonDataLength',
  deps: [procurementsByStatusData, procurementsByMethodData],
  mapper: (a, b) => Math.min(
    5,
    Math.max(a.length, b.length)
  )
});

export const max2ndRowCommonDataLength = PEState.mapping({
  name: 'max2ndRowCommonDataLength',
  deps: [winsAndFlagsData, PEFlaggedNrData],
  mapper: (a, b) => Math.min(
    5,
    Math.max(a.length, b.length)
  ),
});
