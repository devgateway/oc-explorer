import frontentYearFilterable from "../../frontend-year-filterable";
import Chart from "./index.jsx";

class FrontendYearFilterableChart extends frontentYearFilterable(Chart){
  hasNoData(){
    let data = super.getData();
    return data && data.isEmpty();
  }
}

FrontendYearFilterableChart.UPDATABLE_FIELDS = ['data', 'years'];

export default FrontendYearFilterableChart;