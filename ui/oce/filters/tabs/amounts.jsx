import Tab from "./index";
class Amount extends Tab{
  render(){
    return <h2>amount</h2>
  }
}

Amount.getName = __ => __('Amount');

export default Amount;