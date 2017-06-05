import Tab from "./index";
import ProcurementMethod from '../procurement-method';

class TenderRules extends Tab{
  render(){
    return (
      <div>
        {this.renderChild(ProcurementMethod, 'procurementMethod')}
      </div>
    )
  }
}

TenderRules.getName = t => t('filters:tabs:procurementRules:title');

export default TenderRules;
