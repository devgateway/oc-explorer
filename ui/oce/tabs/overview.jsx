import Tab from "./index";
import OverviewChart from "../visualizations/charts/overview";
import TendersTable from "../visualizations/tables/tenders";
import AwardsTable from "../visualizations/tables/awards";

class Overview extends Tab{
  static getName(__){
    return __("Overview")
  }
}

Overview.visualizations = [OverviewChart, TendersTable, AwardsTable];

Overview.icon = "overview";

export default Overview;