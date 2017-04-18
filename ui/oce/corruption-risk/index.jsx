import style from "./style.less";
import cn from "classnames";
import URI from "urijs";
import {fetchJson} from "../tools";
import OverviewPage from "./overview-page";
import CorruptionTypePage from "./corruption-type";
import {Map, Set} from "immutable";
import IndividualIndicatorPage from "./individual-indicator";
import Chart from "../visualizations/charts/index.jsx";
import Plotly from "plotly.js/lib/core";
import Filters from "./filters";

const ROLE_ADMIN = 'ROLE_ADMIN';

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
        width,
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
      user: {
        loggedIn: false,
        isAdmin: false
      },
      page: 'overview',
      indicatorTypesMapping: {},
      filters: Map(),
			years: Set(),
			months: Set(),
      filterBoxIndex: null
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
        location.href = '/login?referrer=/ui/index.html?corruption-risk-dashboard'
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
      return (
			 <a href="/preLogout?referrer=/ui/index.html?corruption-risk-dashboard">
				 <button className="btn btn-success">Logout</button>
       </a>
			)
    }
    return <a href="/login?referrer=/ui/index.html?corruption-risk-dashboard">
        <button className="btn btn-success">Login</button>
    </a>
  }

  getPage(){
		const {translations, styling} = this.props;
    const {page, filters, years, months} = this.state;
		const monthly = years.count() == 1;
    if(page == 'overview'){
      return <OverviewPage
                 filters={filters}
								 translations={translations}
								 years={years}
								 monthly={monthly}
								 months={months}
                 styling={styling}
             />;
    } else if(page == 'corruption-type') {
      const {corruptionType, indicatorTypesMapping} = this.state;
      const indicatorType = {
        fraud: 'FRAUD',
        process_rigging: 'RIGGING',
        collusion: 'COLLUSION'
      }[corruptionType];

      const indicators =
        Object.keys(indicatorTypesMapping).filter(key => indicatorTypesMapping[key].types.indexOf(indicatorType) > -1);

      return <CorruptionTypePage
                 indicators={indicators}
                 onGotoIndicator={individualIndicator => this.setState({page: 'individual-indicator', individualIndicator})}
                 filters={filters}
								 translations={translations}
                 corruptionType={corruptionType}
								 years={years}
								 monthly={monthly}
								 months={months}
             />;
    } else if(page == 'individual-indicator'){
      const {individualIndicator} = this.state;
      return (
				<IndividualIndicatorPage
						indicator={individualIndicator}
						filters={filters}
						translations={translations}
						years={years}
						monthly={monthly}
						months={months}
				/>
			)
    }
  }

  render(){
    const {dashboardSwitcherOpen, corruptionType, page, filters, years, months, filterBoxIndex} = this.state;
    const {onSwitch, translations} = this.props;
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
           onClick={e => this.setState({dashboardSwitcherOpen: false, filterBoxIndex: null})}
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
        <Filters
            onUpdate={filters => this.setState({
              filters: filters.delete('years').delete('months'),
              years: filters.get('years'),
              months: filters.get('months')
            })}
            translations={translations}
            currentBoxIndex={filterBoxIndex}
            requestNewBox={index => this.setState({filterBoxIndex: index})}
        />
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
              filters={filters}
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
