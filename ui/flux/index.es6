import dispatcher from "./dispatcher";
import actions from "./actions";
import globalStateStore from "./stores/global-state";

dispatcher.registerStores({
  globalState: globalStateStore
});

export default {
  actions: actions,
  onUpdate(cb, trigger = false){
    dispatcher.observe([], cb);
    if(trigger) cb(dispatcher.evaluate([]));
  }
}