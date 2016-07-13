import App from "./components/app";
import React from "react";
import ReactDOM from "react-dom";
import flux from "./flux";
import {debounce} from "./tools";
import OCApp from "./oce";
import styles from "./style.less";

var TRANSLATIONS = {
  en: require('./languages/en_US.json'),
  vn: require('./languages/vn_VN.json')
};

class OCVN extends OCApp{
  constructor(props) {
    super(props, {
      tabs: [{
        name: () => this.__("Overview"),
        icon: "search",
        sections: [{

        }]
      }, {
        name: () => this.__("Location"),
        icon: "map-marker",
        Component: () => <h1>here be map</h1>
      }, {
        name: () => this.__("Competitiveness")
      }, {
        name: () => this.__("Efficiency")
      }, {
        name: () => this.__("eProcurement")
      }]
      /*

       {function(tab, props){
       let {state, actions} = props;
       switch(tab){
       case tabs.OVERVIEW:
       return <Overview
       translations={translations}
       actions={actions}
       state={state.get('overview')}
       width={width}
       />;
       case tabs.PLANNING: return (
       <Planning
       translations={translations}
       width={globalState.get('contentWidth')}
       years={globalState.getIn(['filters', 'years'])}
       locations={globalState.getIn(['data', 'locations'])}
       />
       );
       case tabs.COMPETITIVENESS: return (
       <Competitiveness
       translations={translations}
       actions={actions}
       state={state.get('competitiveness')}
       width={width}
       />
       );
       case tabs.EFFICIENCY: return (
       <Efficiency
       translations={translations}
       actions={actions}
       state={state.get('efficiency')}
       width={width}
       />
       );
       case tabs.E_PROCUREMENT: return (
       <EProcurement
       translations={translations}
       actions={actions}
       state={state.get('eProcurement')}
       width={width}
       />
       );
       }
       }(globalState.get('tab'), this.props)}

       {globalState.hasIn(['filters', 'years']) && globalState.getIn(['filters', 'years']).map((selected, year) => (
       <a
       key={year}
       href="javascript:void(0);"
       className={cn({active: true === selected})}
       onClick={e => actions.toggleYear(year, !selected)}
       >
       <i className="glyphicon glyphicon-ok-circle"></i> {year}
       </a>
       )).toArray()}
       */
    });
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
          <div>
            <i className="glyphicon glyphicon-download-alt"></i> Export <i className="glyphicon glyphicon-menu-down"></i>
          </div>
        </div>
        <div className="col-sm-1 language-switcher">
          <img src="assets/flags/us.png" alt="" onClick={e => actions.setLocale("en")}/>
          <img src="assets/flags/vn.png" alt="" onClick={e => actions.setLocale("vn")}/>
        </div>
      </header>
      <aside className="col-xs-4 col-md-3 col-lg-2">
        <div className="row">
          <div role="navigation">
            {this.navigation()}
          </div>
          <section className="col-sm-12 description">
            <p><strong>{this.__("Toolkit description")}</strong></p>
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
          <div className="col-sm-12 content">
            {this.content()}
          </div>
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

window.addEventListener("resize", debounce(function(){
  flux.actions.changeContentWidth(document.querySelector('.years-bar').offsetWidth)
}));


if("ocvn.developmentgateway.org" == location.hostname){
  (function(b,o,i,l,e,r){b.GoogleAnalyticsObject=l;b[l]||(b[l]=
      function(){(b[l].q=b[l].q||[]).push(arguments)});b[l].l=+new Date;
    e=o.createElement(i);r=o.getElementsByTagName(i)[0];
    e.src='//www.google-analytics.com/analytics.js';
    r.parentNode.insertBefore(e,r)}(window,document,'script','ga'));
  ga('create','UA-78202947-1');ga('send','pageview');
}