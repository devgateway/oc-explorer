import { Set } from 'immutable';
import { CRD, datefulFilters, API_ROOT } from '../../state/oce-state';

export const PEIds = CRD.input({
  name: 'PEIds'
});

export const PEsFilters = CRD.mapping({
  name: 'PEsFilters',
  deps: [datefulFilters, PEIds],
  mapper: (filters, PEId) => filters.update(
    'procuringEntityId',
    Set(),
    PEIds => PEIds.add(PEId)
  )
});

const TendersCountEP = CRD.input({
  name: 'TendersCountEP',
  initial: `${API_ROOT}/procuringEntitiesTendersCount`,
});

const AwardsCountEP = CRD.input({
  name: 'AwardsCountEP',
  initial: `${API_ROOT}/procuringEntitiesAwardsCount`,
});

const TendersCountRaw = CRD.remote({
  name: 'TendersCountRaw',
  url: TendersCountEP,
  params: PEsFilters,
});

export const TendersCount = CRD.mapping({
  name: 'TendersCount',
  deps: [TendersCountRaw],
  mapper: raw => {
    const result = {};
    raw.forEach(({ _id, tenderCount }) => {
      result[_id] = tenderCount
    });
    return result;
  },
});

const AwardsCountRaw = CRD.remote({
  name: 'AwardsCountRaw',
  url: AwardsCountEP,
  params: PEsFilters,
});

export const AwardsCount = CRD.mapping({
  name: 'AwardsCount',
  deps: [AwardsCountRaw],
  mapper: raw => {
    const result = {};
    raw.forEach(({ _id, awardCount }) => {
      result[_id] = awardCount
    });
    return result;
  },
})
