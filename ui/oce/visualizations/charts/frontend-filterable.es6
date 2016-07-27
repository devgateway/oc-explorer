import frontentYearFilterable from "../../frontend-year-filterable";
import Chart from "./index.jsx";
import Plotly from "plotly.js/lib/core";

class FrontendYearFilterableChart extends frontentYearFilterable(Chart){}

FrontendYearFilterableChart.UPDATABLE_FIELDS = ['data', 'years'];

export default FrontendYearFilterableChart;