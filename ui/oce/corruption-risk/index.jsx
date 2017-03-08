import style from "./style.less";
import cn from "classnames";
import URI from "urijs";
import {fetchJson} from "../tools";

const ROLE_ADMIN = 'ROLE_ADMIN';

class CorruptionRiskDashboard extends React.Component{
  constructor(...args){
    super(...args);
    this.state={
      dashboardSwitcherOpen: false,
      user: {
        loggedIn: false,
        isAdmin: false
      }
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
      () => this.setState({
        user: {
          loggedIn: false
        }
      })
    )
  }

  componentDidMount(){
    this.fetchUserInfo();
  }

  toggleDashboardSwitcher(e){
    e.stopPropagation();
    const {dashboardSwitcherOpen} = this.state;
    this.setState({dashboardSwitcherOpen: !dashboardSwitcherOpen});
  }

  loginBox(){
    console.log(this.state.user);
    if(this.state.user.loggedIn){
      return <a href="/preLogout?referrer=/ui/index.html">
<button className="btn btn-success">Logout</button>
      </a>
    }
    return <a href="/login?referrer=/ui/index.html">
        <button className="btn btn-success">Login</button>
    </a>
  }

  render(){
    const {dashboardSwitcherOpen} = this.state;
    const {onSwitch} = this.props;
    return (
      <div className="container-fluid corruption-risk-dashboard"
           onClick={e => this.setState({dashboardSwitcherOpen: false})}
      >
        <header className="branding row">
          <div className="col-sm-1 logo-wrapper">
            <img src="assets/logo.png"/>
          </div>
          <div className="col-sm-8">
            <div className={cn('dash-switcher-wrapper', {open: dashboardSwitcherOpen})}>
              <h1 onClick={this.toggleDashboardSwitcher.bind(this)}>
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
      </div>
    )
  }
}

export default CorruptionRiskDashboard;
