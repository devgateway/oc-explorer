import { Map, Set } from 'immutable';
import State from './index';
const API_ROOT = '/api';

const state = new State();

state.input({
  name: 'filters',
  initial: Map()
});

state.map({
  name: 'datelessFilters',
  deps: ['filters'],
  mapper: filters => filters.delete('years').delete('months')
});

state.input({
  name: 'supplierId'
});

state.map({
  name: 'supplierFilters',
  deps: ['filters', 'supplierId'],
  mapper: (filters, supplierId) => 
    filters.update('supplierId', Set(), supplierIds => supplierIds.add(supplierId))
});

state.map({
  name: 'supplierDetailsURL',
  deps: ['supplierId'],
  mapper: id => `${API_ROOT}/ocds/organization/supplier/id/${id}`
});

state.endpoint({
  name: 'supplierDetails',
  url: 'supplierDetailsURL'
})

export default state;