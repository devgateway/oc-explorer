import ReactDOM from "react-dom";
import OCApp from "./oce";
import OverviewTab from './oce/tabs/overview';
import LocationTab from './oce/tabs/location';
import CompetitivenessTab from './oce/tabs/competitiveness';
import EfficiencyTab from './oce/tabs/efficiency';
import EProcurementTab from './oce/tabs/e-procurement';
import styles from "./style.less";

var TRANSLATIONS = {
  en: require('./languages/en_US.json'),
  vn: require('./languages/vn_VN.json')
};

class OCVN extends OCApp{
  constructor(props) {
    super(props);
    this.registerTab(OverviewTab);
    this.registerTab(LocationTab);
    this.registerTab(CompetitivenessTab);
    this.registerTab(EfficiencyTab);
    this.registerTab(EProcurementTab);
  }

  render(){
    return <div className="container-fluid">
      <header className="branding row">
        <div className="col-sm-offset-1 col-sm-4">
          <h1>
            {this.__('e-Procurement')}
            <small>{this.__('Toolkit')}</small>
          </h1>
        </div>
        <div className="col-sm-6 menu">
          {this.filters()}
          {this.comparison()}
          <div className="filters">
            <img className="top-nav-icon" src="assets/icons/export.svg"/> {this.__('Export')} <i className="glyphicon glyphicon-menu-down"></i>
          </div>
        </div>
        <div className="col-sm-2 language-switcher">
          <img className="flag" src="assets/flags/us.png" alt="US flag" onClick={e => actions.setLocale("en")}/>
          <img className="flag" src="assets/flags/vn.png" alt="Vietnam flag" onClick={e => actions.setLocale("vn")}/>
        </div>
      </header>
      <aside className="col-xs-4 col-md-3 col-lg-2">
        <div className="row">
          <div role="navigation">
            {this.navigation()}
          </div>
          <section className="col-sm-12 description">
            <h3><strong>{this.__("Toolkit description")}</strong></h3>
            <p>
              <small>
                {this.__("The Procurement M&E Prototype is an interactive platform for analyzing, monitoring, and evaluating information on procurement in Vietnam. All data in the dashboard are collected from the Vietnam Government eProcurement system (eGP).")}
              </small>
            </p>
          </section>
        </div>
      </aside>
      <div className="col-xs-offset-4 col-md-offset-3 col-lg-offset-2 col-xs-8 col-md-9 col-lg-10">
        <div className="row">
          {this.content()}
        </div>
      </div>
      <div className="col-xs-offset-4 col-md-offset-3 col-lg-offset-2 col-xs-8 col-md-9 col-lg-10 years-bar" role="navigation">
        {this.yearsBar()}
      </div>
      <footer className="col-sm-12 main-footer">&nbsp;</footer>
    </div>;
  }
}

ReactDOM.render(<OCVN/>, document.getElementById('dg-container'));

if("ocvn.developmentgateway.org" == location.hostname){
  (function(b,o,i,l,e,r){b.GoogleAnalyticsObject=l;b[l]||(b[l]=
      function(){(b[l].q=b[l].q||[]).push(arguments)});b[l].l=+new Date;
    e=o.createElement(i);r=o.getElementsByTagName(i)[0];
    e.src='//www.google-analytics.com/analytics.js';
    r.parentNode.insertBefore(e,r)}(window,document,'script','ga'));
  ga('create','UA-78202947-1');ga('send','pageview');
}