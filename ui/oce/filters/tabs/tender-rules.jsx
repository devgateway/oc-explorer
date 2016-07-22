import Tab from "./index";

class TenderRules extends Tab{
  render(){
    return <h2>tender rules</h2>
  }
}

TenderRules.getName = __ => __('Tender rules');

export default TenderRules;