import cn from "classnames";
import {fromJS, Map, Set} from "immutable";
import {fetchJson, debounce} from "./tools";
import URI from "urijs";
import Filters from "./filters";

let range = (from, to) => from > to ? [] : [from].concat(range(from + 1, to));
const MIN_YEAR = 2010;
const MAX_YEAR = 2020;
const MENU_BOX_COMPARISON = "menu-box";
const MENU_BOX_FILTERS = 'filters';

export default class OCApp extends React.Component{
  constructor(props){
    super(props);
    this.tabs = [];
    this.state = {
      width: 0,
      currentTab: 0,
      menuBox: MENU_BOX_FILTERS,
      compareBy: "",
      comparisonCriteriaValues: [],
      selectedYears: Set(range(MIN_YEAR, MAX_YEAR)),
      filters: fromJS({}),
      data: fromJS({}),
      comparisonData: fromJS({}),
      bidTypes: fromJS({})
    }
  }

  registerTab(tab){
    this.tabs.push(tab);
  }

  __(text){
    return text;
  }

  updateComparisonCriteria(criteria){
    this.setState({
      menuBox: "",
      compareBy: criteria,
      comparisonCriteriaValues: [],
      comparisonData: fromJS({})
    });
    if(!criteria) return;
    fetchJson(new URI('/api/costEffectivenessTenderAmount').addSearch({
      groupByCategory: criteria,
      pageSize: 3
    })).then(data => this.setState({
      comparisonCriteriaValues: data.map(datum => datum[0] || datum._id)
    }));
  }

  fetchBidTypes(){
    fetchJson('/api/ocds/bidType/all').then(data =>
        this.setState({bidTypes: data.reduce((map, datum) => map.set(datum.id, datum.description), Map())})
    );
  }

  componentDidMount(){
    this.fetchBidTypes();

    this.setState({
      width: document.querySelector('.years-bar').offsetWidth
    });

    window.addEventListener("resize", debounce(() => {
      this.setState({
        width: document.querySelector('.years-bar').offsetWidth
      });
    }));
  }

  setMenuBox(e, slug){
    let {menuBox} = this.state;
    e.stopPropagation();
    this.setState({menuBox: menuBox == slug ? "" : slug})
  }

  filters(){
    return <div className="filters">
      <img className="top-nav-icon" src="assets/icons/filter.svg"/> {this.__('Filter the data')} <i className="glyphicon glyphicon-menu-down"></i>
    </div>
  }

  setMenuBox(e, slug){
    let {menuBox} = this.state;
    e.stopPropagation();
    this.setState({menuBox: menuBox == slug ? "" : slug})
  }

  filters(){
    let {menuBox} = this.state;
    return <this.constructor.Filters
        onClick={e => this.setMenuBox(e, MENU_BOX_FILTERS)}
        onUpdate={filters => this.setState({filters, menuBox: ""})}
        open={menuBox == MENU_BOX_FILTERS}
    />
  }

  comparison(){
    let {menuBox, compareBy} = this.state;
    return <div
        onClick={e => this.setMenuBox(e, MENU_BOX_COMPARISON)}
        className={cn("filters compare", {open: menuBox == MENU_BOX_COMPARISON})}
    >
      <img className="top-nav-icon" src="assets/icons/compare.svg"/> {this.__('Compare')} <i className="glyphicon glyphicon-menu-down"></i>
      <div className="box" onClick={e => e.stopPropagation()}>
        <div className="col-sm-6">
          <label>{this.__('Comparison criteria')}</label>
        </div>
        <div className="col-sm-6">
          <select
              className="form-control"
              value={compareBy}
              onChange={e => this.updateComparisonCriteria(e.target.value)}
          >
            <option value="">{this.__('None')}</option>
            <option value="bidTypeId">{this.__('Bid Type')}</option>
            <option value="bidSelectionMethod">{this.__('Bid Selection Method')}</option>
            <option value="procuringEntityId">{this.__('Procuring Entity')}</option>
          </select>
        </div>
      </div>
    </div>;
  }

  navigationLink({name, icon}, index){
    return <a href="javascript:void(0);" key={index}
              className={cn("col-sm-12", {active: index == this.state.currentTab})}
              onClick={_ => this.setState({currentTab: index})}>
          <span className="circle">
            <img className="nav-icon" src={`assets/icons/${icon}.svg`}/>
            <i className={`glyphicon glyphicon-${icon}`}/>
          </span>
      &nbsp;
      {name(this.__)}
    </a>
  }

  navigation(){
    return this.tabs.map((tab, index) => this.navigationLink(tab, index));
  }

  updateData(path, data){
    this.setState({data: this.state.data.setIn(path, data)});
  }

  updateComparisonData(path, data){
    this.setState({comparisonData: this.state.comparisonData.setIn(path, data)})
  }

  content(){
    let {filters, compareBy, comparisonCriteriaValues, currentTab, selectedYears, bidTypes, width} = this.state;
    let Tab = this.tabs[currentTab];
    return <Tab
        filters={filters}
        compareBy={compareBy}
        comparisonCriteriaValues={comparisonCriteriaValues}
        requestNewData={(path, data) => this.updateData([currentTab, ...path], data)}
        requestNewComparisonData={(path, data) => this.updateComparisonData([currentTab, ...path], data)}
        data={this.state.data.get(currentTab) || fromJS({})}
        comparisonData={this.state.comparisonData.get(currentTab) || fromJS({})}
        years={selectedYears}
        bidTypes={bidTypes}
        width={width}
    />;
  }

  yearsBar(){
    let {currentTab, compareBy, data, selectedYears, comparisonData} = this.state;
    let tab = this.tabs[currentTab];
    let years = (compareBy && tab.computeComparisonYears ?
            tab.computeComparisonYears(comparisonData.get(currentTab)) :
            tab.computeYears(data.get(currentTab))
    );

    return years.sort().map(year =>
        <a
            key={year}
            href="javascript:void(0);"
            className={cn({active: selectedYears.has(+year)})}
            onClick={_ => this.setState({
              selectedYears: selectedYears.has(+year) ?
                  selectedYears.delete(+year) :
                  selectedYears.add(+year)
            })}
        >
          <i className="glyphicon glyphicon-ok-circle"></i> {year}
        </a>
    ).toArray();
  }
}
