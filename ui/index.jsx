import ReactDOM from "react-dom";
import OCApp from "./oce";
import OverviewTab from './oce/tabs/overview';
import OCVNLocation from "./ocvn/tabs/location";
import OCVNCompetitiveness from './ocvn/tabs/competitiveness';
import EfficiencyTab from './oce/tabs/efficiency';
import EProcurementTab from './oce/tabs/e-procurement';
import {fetchJson} from "./oce/tools";
import {Map} from "immutable";
import OCVNFilters from "./ocvn/filters";
import styles from "./style.less";

function getBidTypeDescription(__, {id, description}){
  switch(+id){
    case 12: return __("Unspecified") + " #1";
    case 15: return __("Unspecified") + " #2";
    default: return description;
  }
}

class OCVN extends OCApp{
  constructor(props) {
    super(props);
    this.registerTab(OverviewTab);
    this.registerTab(OCVNLocation);
    this.registerTab(OCVNCompetitiveness);
    this.registerTab(EfficiencyTab);
    this.registerTab(EProcurementTab);
  }

  fetchBidTypes(){
    fetchJson('/api/ocds/bidType/all').then(data =>
        this.setState({
          bidTypes: data.reduce((map, datum) =>
            map.set(datum.id, getBidTypeDescription(this.__.bind(this), datum)), Map())
        })
    );
  }

  render(){
    return <div className="container-fluid" onClick={_ => this.setState({menuBox: ""})}>
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
          {this.exportBtn()}
        </div>
        <div className="col-sm-2 language-switcher">
          {this.languageSwitcher()}
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

OCVN.Filters = OCVNFilters;

OCVN.TRANSLATIONS = {
  us: {},
  vn: require('./languages/vn_VN.json')
};

OCVN.STYLING = {
  charts: {
    axisLabelColor: "#cc3c3b",
    traceColors: ["#234e6d", "#3f7499", "#80b1d3", "#afd5ee", "#d9effd"]
  }
};

ReactDOM.render(<OCVN/>, document.getElementById('dg-container'));

if("ocvn.developmentgateway.org" == location.hostname){
  (function(b,o,i,l,e,r){b.GoogleAnalyticsObject=l;b[l]||(b[l]=
      function(){(b[l].q=b[l].q||[]).push(arguments)});b[l].l=+new Date;
    e=o.createElement(i);r=o.getElementsByTagName(i)[0];
    e.src='//www.google-analytics.com/analytics.js';
    r.parentNode.insertBefore(e,r)}(window,document,'script','ga'));
  ga('create','UA-78202947-1');ga('send','pageview');
}