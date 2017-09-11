import FilterBox from "./box";
import {Set} from "immutable";
import ProcurementMethod from '../../filters/procurement-method.jsx';

class ProcurementMethodBox extends FilterBox{
  isActive(){
    const {appliedFilters} = this.props;
    return appliedFilters.get('procurementMethod', Set()).count() > 0;
  }

  reset(){
    const {onApply, state} = this.props;
    onApply(state.delete('procurementMethod'));
  }

  getTitle(){
    return this.t('filters:tabs:procurementMethod:title');
  }

  getBox(){
    return (
      <div className="box-content">
        {this.renderChild(ProcurementMethod, 'procurementMethod')}
      </div>
    )
  }
}

export default ProcurementMethodBox;
