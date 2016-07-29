import Tab from "./index";
import CostEffectiveness from "../visualizations/charts/cost-effectiveness";
import BidSelectionMethod from "../visualizations/charts/bid-selection-method";
import AvgNrBids from "../visualizations/charts/avg-nr-bids";

class Competitiveness extends Tab{
  static getName(__){return __('Competitiveness')}
}

Competitiveness.icon = "competitive";
Competitiveness.visualizations = [CostEffectiveness, BidSelectionMethod, AvgNrBids];
export default Competitiveness;