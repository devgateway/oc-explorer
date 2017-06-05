import Tab from "./index";

class TenderRules extends Tab{
  render(){
    return <h2>tender rules</h2>
  }
}

TenderRules.getName = t => t('filters:tabs:procurementRules:title');

export default TenderRules;
