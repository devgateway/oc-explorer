class LandingPopup extends React.Component{
  render(){
    return (
      <div>
        <div className="crd-landing-popup-overlay"/>
        <div className="crd-landing-popup">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-1 text-right">
                <img src="assets/logo.png"/>
              </div>
              <div className="col-sm-11">
                <h4 className="popup-title">Corruption Risk Dashboard</h4>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-5 col-sm-offset-1 text-column-left">
                The Corruption Risk Dashboard (CRD) is an open source tool that aims to help users understand the potential presence of corruption in public contracting. Through a red flagging approach (highlighting procurement activities that have certain risk factors), this prototype explores corruption risk through the visualization of 10 indicators that are mapped to three different forms of corruption: fraud, collusion, and process rigging. Users are free to explore the data, which has been converted to the Open Contracting Data Standard (OCDS), using a variety of filters. A crosstab table enables users to explore the overlap between any two indicators that are mapped to the same risk type.
              </div>
              <div className="col-sm-5 end text-column-right">
                The methodological approach that informs the CRD, which was built by Development Gateway (DG) with the collaboration and support of the Open Contracting Partnership (OCP), is presented and co-authored research paper. Explanations of the principal concepts and indicators are avaible within the Dashboard.
              </div>

              <hr className="col-sm-offset-1 col-sm-10 end separator"/>

              <div className="col-sm-offset-1 col-sm-11 section-title">
                <h5>
                  Intended Use:&nbsp;
                  <small>
                    The CRD was designed with the primary objective of supporting two specific use cases:
                  </small>
                </h5>
              </div>
              <ol>
                <li className="col-sm-5 col-sm-offset-1 text-column-left">
                  To aid these individuals to monitor corruption risk in procurement markets over time.
                </li>
                <li className="col-sm-5 end text-column-right">
                  To aid these individuals to monitor corruption risk in procurement markets over time.
                </li>
              </ol>

              <hr className="col-sm-offset-1 col-sm-10 end separator"/>

              <div className="col-sm-offset-1 col-sm-11 section-title">
                <h5>
                  Distinguishing between &ldquo;Corruption&rdquo; and &ldquo;Corruption Risk&rdquo;
                </h5>
              </div>

              <div className="col-sm-5 col-sm-offset-1 text-column-left">
                While flags associated with a procurement process may indicate the possibility that corruption has taken place, they may also be attributable to poor data quality, systemic weaknesses, or practices that may be appropriate when specifically authorized by a procurement authority or regulatory institution. For this reason, this tool is best viewed as a mechanism for identifying the &ldquo;risk&rdquo; of corruption, rathen than &ldquo;corruption&rdquo; itself.
              </div>
              <div className="col-sm-5 end text-column-right">
                Furthermore, in some instances, a single flag &mdash; such as for the awarding of two contracts to the same supplier by a single procuring entity &mdash; may not show any evidence of wrongdoing, though the confluence of multiple indicators suggests greater cause of concern.
              </div>

              <hr className="col-sm-offset-1 col-sm-10 end separator"/>

              <div className="col-sm-11 col-sm-offset-1 section-title">
                <h5>Your feedback is welcome</h5>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default LandingPopup;
