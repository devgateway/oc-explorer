import Tab from "./index";
import BidPeriod from "../visualizations/charts/bid-period";
import Cancelled from "../visualizations/charts/cancelled";

class Efficiency extends Tab{
  static getName(__){return __('Efficiency')}
}

Efficiency.icon = "efficiency";
Efficiency.visualizations = [BidPeriod, Cancelled];

export default Efficiency;