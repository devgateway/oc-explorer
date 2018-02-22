import { Map } from 'immutable';
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
})

export default state;