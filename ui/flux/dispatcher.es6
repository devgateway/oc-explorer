import {Reactor} from "nuclear-js";

export default new Reactor({
  debug: localStorage && !!localStorage.debug
});