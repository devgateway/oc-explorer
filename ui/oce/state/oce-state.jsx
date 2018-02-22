import { Map, Set } from 'immutable';
import State from './index';

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
})

export default state;