import { PEState, PEFilters } from '../state';
import { API_ROOT } from '../../../../state/oce-state';

export const PETableState = PEState.substate({
  name: 'PETableState',
});

const procurementsEP = PEState.input({
  name: 'procurementsEP',
  initial: `${API_ROOT}/flaggedRelease/all`,
});

export const page = PEState.input({
  name: 'page',
  initial: 1,
});

export const pageSize = PEState.input({
  name: 'pageSize',
  initial: 20,
});

const filters = PEState.mapping({
  name: 'filters',
  deps: [PEFilters, page, pageSize],
  mapper: (PEFilters, page, pageSize) =>
    PEFilters
      .set('pageSize', pageSize)
      .set('pageNumber', page - 1)
});

const procurementsRaw = PEState.remote({
  name: 'procurementsRaw',
  url: procurementsEP,
  params: filters,
});

const findActiveAward = awards =>
  awards.find(
    award => award.status === 'active'
  );

function getAwardAmount(awards) {
  const award = findActiveAward(awards);
  if (!award) return 0;
  const { value } = award;
  return `${value.amount} ${value.currency}`;
}

function getTenderAmount(datum) {
  try {
    return `${datum.tender.value.amount} ${datum.tender.value.currency}`;
  } catch(whatever) {
    return 0;
  }
}

export const procurementsData = PEState.mapping({
  name: 'procurementsData',
  deps: [procurementsRaw],
  mapper: raw =>
    raw.map(datum => {
      return {
        id: datum.id,
        name: datum.tender.title || 'N/A',
        ocid: datum.ocid,
        awardStatus: getAwardAmount(datum.awards) ? 'active' : 'unsuccessful',
        tenderAmount: getTenderAmount(datum),
        awardAmount: getAwardAmount(datum.awards),
        nrBidders: datum.tender.numberOfTenderers || 0,
        nrFlags: datum.flags.totalFlagged,
        flags: Object.keys(datum.flags).filter(key => datum.flags[key].value),
      }
    })
})

const procurementsCountEP = PEState.input({
  name: 'procurementsCountEP',
  initial: `${API_ROOT}/flaggedRelease/count`,
});

export const procurementsCount = PEState.remote({
  name: 'procurementsCount',
  url: procurementsCountEP,
  params: PEFilters,
});
