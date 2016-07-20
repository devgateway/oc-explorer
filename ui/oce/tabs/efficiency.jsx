import Tab from "./index";
import BidPeriod from "../visualizations/charts/bid-period";
import Cancelled from "../visualizations/charts/cancelled";

class Efficiency extends Tab{
  static name(__){return __('Efficiency')}
}

Efficiency.visualizations = [BidPeriod, Cancelled];

export default Efficiency;