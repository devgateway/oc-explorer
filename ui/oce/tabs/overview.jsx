import Tab from "./index";
import OverviewChart from "../visualizations/charts/overview";
import TendersTable from "../visualizations/tables/tenders";
import AwardsTable from "../visualizations/tables/awards";

class Overview extends Tab{
  static name(__){
    return __("Overview")
  }
}

Overview.visualizations = [OverviewChart, TendersTable, AwardsTable];

Overview.icon = "search";

export default Overview;