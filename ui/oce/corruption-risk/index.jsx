import style from "./style.less";
import cn from "classnames";
import URI from "urijs";
import {fetchJson} from "../tools";
import OverviewPage from "./overview-page";
import CorruptionTypePage from "./corruption-type";
import {Map} from "immutable";
import IndividualIndicatorPage from "./individual-indicator";

const ROLE_ADMIN = 'ROLE_ADMIN';

class Filter extends React.Component{
  render(){
    const {title, open, onClick} = this.props;
    return(
      <div onClick={onClick} className={cn('filter', {open})}>
        {title}
        <i className="glyphicon glyphicon-menu-down"></i>
        {open && <div className="dropdown">
        </div>}
      </div>
    )
  }
}

class Filters extends React.Component{
  render(){
    const {box, requestNewFilterBox} = this.props;
    const setBox = box => e => {
      e.stopPropagation();
      requestNewFilterBox(box);
    };

    const filters = [{
      title: "Organizations",
      slug:  "organizations"
    },{
      title: "Procurement method",
      slug:  "procurementMethod"
    },{
      title: "Value Amount",
      slug:  "valueAmount"
    },{
      title: "Date",
      slug:  "date"
    },{
      title: "Location",
      slug:  "location"
    }];
    return (
      <div className="row filters-bar">
        <div className="col-lg-1 col-md-1 col-sm-1">
        </div>
        <div className="col-lg-8 col-md-9 col-sm-9">
        <div className="title">
          Filter your data
        </div>
	      {filters.map(({title, slug}, index) => (
  	      <Filter
    	    	title={title}
	        	open={box == slug}
		        onClick={setBox(slug)}
  	        key={index}
    	    />
        ))}
        </div>
        <div className="col-lg-2 col-md-1 col-sm-1 download">
          <button className="btn btn-success">
            <i className="glyphicon glyphicon-download-alt"></i>
          </button>
        </div>
        <div class="col-lg-1 col-md-1">
        </div>
      </div>
    )
  }
}

import Chart from "../visualizations/charts/index.jsx";
import Plotly from "plotly.js/lib/core";
Plotly.register([
  require('plotly.js/lib/pie')
]);

class TotalFlags extends Chart{
  constructor(...args){
    super(...args);
    this.state = {

    }
  }

  getData(){
    return [{
      values: [22565, 5450, 5850],
      labels: ["Fraud", "Process rigging", "Collusion"],
      textinfo: 'value',
      hole: .85,
      type: 'pie'
    }];
  }

  getLayout(){
    const {width} = this.props;
    return {
      legend: {
        orientation: 'h',
        height: 50,
        x: '0',
        y: '0'
      },
      paper_bgcolor: 'rgba(0,0,0,0)'
    }
  }
}

class CorruptionRiskDashboard extends React.Component{
  constructor(...args){
    super(...args);
    this.state={
      dashboardSwitcherOpen: false,
      filterBox: "",
      user: {
        loggedIn: false,
        isAdmin: false
      },
      page: 'overview',
      indicatorTypesMapping: {}
    }
  }

  fetchUserInfo(){
    const noCacheUrl = new URI('/rest/userDashboards/getCurrentAuthenticatedUserDetails').addSearch('time', Date.now());
    fetchJson(noCacheUrl).then(
      ({username, id, roles}) => this.setState({
        user: {
          loggedIn: true,
          isAdmin: roles.some(({authority}) => authority == ROLE_ADMIN),
          id
        }
      })
    ).catch(
      err => {
        alert('You must be logged in to access Corruption Risk Dashboard');
        location.href = '/login?referrer=/ui/index.html'
      }
    )
  }

  fetchIndicatorTypesMapping(){
    fetchJson('/api/indicatorTypesMapping').then(data => this.setState({indicatorTypesMapping: data}));
  }

  componentDidMount(){
    this.fetchUserInfo();
    this.fetchIndicatorTypesMapping()
  }

  toggleDashboardSwitcher(e){
    e.stopPropagation();
    const {dashboardSwitcherOpen} = this.state;
    this.setState({dashboardSwitcherOpen: !dashboardSwitcherOpen});
  }

  loginBox(){
    if(this.state.user.loggedIn){
      return <a href="/preLogout?referrer=/ui/index.html">
				<button className="btn btn-success">Logout</button>
      </a>
    }
    return <a href="/login?referrer=/ui/index.html">
        <button className="btn btn-success">Login</button>
    </a>
  }

  getPage(){
    const {page} = this.state;
    if(page == 'overview'){
      return <OverviewPage/>;
    } else if(page == 'corruption-type') {
      const {corruptionType, indicatorTypesMapping} = this.state;
      const indicatorType = {
        process_rigging: 'RIGGING'
      }[corruptionType];

      const indicators =
        Object.keys(indicatorTypesMapping).filter(key => indicatorTypesMapping[key].types.indexOf(indicatorType) > -1);

      return <CorruptionTypePage
                 indicators={indicators}
                 onGotoIndicator={individualIndicator => this.setState({page: 'individual-indicator', individualIndicator})}
             />;
    } else if(page == 'individual-indicator'){
      const {individualIndicator} = this.state;
      return <IndividualIndicatorPage
                 indicator={individualIndicator}
             />
    }
  }

  render(){
    const {dashboardSwitcherOpen, filterBox, corruptionType, page} = this.state;
    const {onSwitch} = this.props;
    const tabs = [{
	    slug: "fraud",
	    name: "Fraud"
    }, {
	    slug: "process_rigging",
	    name: "Process rigging"
    }, {
	    slug: "collusion",
	    name: "Collusion"
    }];
    return (
      <div className="container-fluid dashboard-corruption-risk"
           onClick={e => this.setState({dashboardSwitcherOpen: false, filterBox: ""})}
      >
        <header className="branding row">
          <div className="col-sm-1 logo-wrapper">
            <img src="assets/logo.png"/>
          </div>
          <div className="col-sm-8">
            <div className={cn('dash-switcher-wrapper', {open: dashboardSwitcherOpen})}>
              <h1 className="corruption-dash-title" onClick={this.toggleDashboardSwitcher.bind(this)}>
                Corruption Risk Dashboard
                <i className="glyphicon glyphicon-menu-down"></i>
              </h1>
              {dashboardSwitcherOpen &&
               <div className="dashboard-switcher">
                 <a href="javascript:void(0);" onClick={e => onSwitch('default')}>
                   Default dashboard
                 </a>
               </div>
              }
            </div>
          </div>
          <div className="col-sm-2 login-wrapper">
            {this.loginBox()}
          </div>
          <div className="col-sm-1">
          </div>
        </header>
        <Filters box={filterBox} requestNewFilterBox={filterBox => this.setState({filterBox})}/>
         <aside className="col-xs-4 col-md-4 col-lg-3">
          <div>
            <h4 className="crd-overview-link" onClick={e => this.setState({page: 'overview'})}>
              Corruption Risk Overview
              <i className="glyphicon glyphicon-info-sign"></i>
            </h4>
            <p className="small">
                The Corruption Risk Dashboard employs a
                red flagging approach to help users understand
                the potential presence of fraud, collusion or
                rigging in public contracting. While flags may
                indicate the presence of corruption, they may
                also be attributable to data quality issues or
                approved practices.
            </p>
          </div>
          <section role="navigation" className="row">
            {tabs.map(({name, slug}, index) =>
              <a
                  href="javascript:void(0);"
                  onClick={e => this.setState({page: 'corruption-type', corruptionType: slug})}
                  className={cn({active: 'corruption-type' == page && slug == corruptionType})}
                  key={index}
              >
                <img src={`assets/icons/${slug}.png`}/>
                {name} <span className="count">(0)</span>
              </a>
						 )}
          </section>
          <TotalFlags
              filters={Map()}
              requestNewData={e => null}
              translations={{}}
              data={Map({a: 1})}
              width={250}
              height={250}
              margin={{l:40, r:40, t:20, b: 10, pad:20}}
          />
        </aside>
        <div className="col-xs-offset-4 col-md-offset-4 col-lg-offset-3 col-xs-8 col-md-8 col-lg-9 content">
          {this.getPage()}
        </div>
      </div>
    )
  }
}

export default CorruptionRiskDashboard;
