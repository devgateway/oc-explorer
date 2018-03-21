import URI from 'urijs';
import { Mapping } from '../../state';
import { indicatorIdsFlat, indicatorTypesMapping, API_ROOT } from '../../state/oce-state';
import { fetchEP } from '../../tools';

export class FlaggedNrMapping extends Mapping {
  constructor({ filters, ...opts }) {
    super({
      deps: [indicatorIdsFlat, filters, indicatorTypesMapping],
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
      ),
      ...opts,
    })
  }
}
