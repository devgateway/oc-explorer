import Tab from "./index";
import ProcuringEntity from "../procuring-entity";
import {Set} from "immutable";

class Organizations extends Tab{
  render(){
    let {state, onUpdate, translations} = this.props;
    let selectedProcuringEntitiesId = state.get('procuringEntityId', Set());
    return <ProcuringEntity
        selected={selectedProcuringEntitiesId}
        onToggle={id => onUpdate('procuringEntityId', selectedProcuringEntitiesId.has(id) ?
            selectedProcuringEntitiesId.delete(id) :
            selectedProcuringEntitiesId.add(id))
        }
        translations={translations}
    />
  }
}

Organizations.getName = __ => __('Organizations');

export default Organizations;