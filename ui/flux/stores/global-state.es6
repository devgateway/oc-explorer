import {Store, toImmutable} from "nuclear-js";
import constants from "../actions/constants";

export default Store({
  getInitialState(){
    return toImmutable({
      counter: 0,
      locale: localStorage.lang || 'en'
    })
  },
  initialize(){
    this.on(constants.INC_COUNTER, state => state.set('counter', state.get('counter') + 1));
    this.on(constants.DEC_COUNTER, state => state.set('counter', state.get('counter') - 1));
    this.on(constants.LOCALE_CHANGED, (state, loc) => state.set('locale', loc));
  }
})