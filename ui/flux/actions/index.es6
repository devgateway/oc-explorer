import dispatcher from '../dispatcher';
import constants from './constants';

export default {
  incCounter() {
    dispatcher.dispatch(constants.INC_COUNTER);
  },
  decCounter() {
    dispatcher.dispatch(constants.DEC_COUNTER);
  },
  setLocale(loc) {
    localStorage.lang = loc;
    dispatcher.dispatch(constants.LOCALE_CHANGED, loc);
  },
};
