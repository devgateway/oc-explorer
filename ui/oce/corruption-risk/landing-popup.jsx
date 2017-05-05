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
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default LandingPopup;
