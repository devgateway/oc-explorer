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

const associatedSuppliers = PEState.mapping({
  name: 'associatedSuppliers',
  deps: ['associatedContracts'],
})

export const PEFlaggedNrData = new FlaggedNrMapping({
  name: 'PEFlaggedNrData',
  filters: PEFilters,
  parent: PEState,
});

export const winsAndFlagsData = PEState.mapping({
  name: 'winsAndFlagsData',
  deps: [associatedContracts],
  mapper: () => [{
    name: 'Mock 1',
    wins: 9000,
    flags: 9001,
  }, {
    name: 'Mock 2',
    wins: 8000,
    flags: 8001,
  }, {
    name: 'Mock 3',
    wins: 7000,
    flags: 7001,
  }, {
    name: 'Mock 4',
    wins: 6000,
    flags: 6001,
  }, {
    name: 'Mock 5',
    wins: 5000,
    flags: 5001,
  }, {
    name: 'Mock 6',
    wins: 4000,
    flags: 4001,
  }, {
    name: 'Mock 7',
    wins: 3000,
    flags: 3001,
  }, {
    name: 'Mock 8',
    wins: 2000,
    flags: 2001,
  }]
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
