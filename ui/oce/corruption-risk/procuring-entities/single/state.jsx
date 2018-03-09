import { Set } from 'immutable';
import { CRD, datefulFilters, API_ROOT } from '../../../state/oce-state';

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

