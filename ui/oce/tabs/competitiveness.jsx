import Tab from "./index";
import CostEffectiveness from "../visualizations/charts/cost-effectiveness";
import AvgNrBids from "../visualizations/charts/avg-nr-bids";

class Competitiveness extends Tab{
  static getName(__){return __('Competitiveness')}
}

Competitiveness.icon = "competitive";
Competitiveness.visualizations = [CostEffectiveness, AvgNrBids];
export default Competitiveness;