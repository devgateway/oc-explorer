import Tab from "./index";
import PercentEProcurement from "../visualizations/charts/percent-e-procurement";
import PercentEBid from "../visualizations/charts/percent-e-bid";

class EProcurement extends Tab{
  static name(__){return __('eProcurement')}
}

EProcurement.visualizations = [PercentEProcurement, PercentEBid];

export default EProcurement;