class LandingPopup extends React.Component{
  render(){
    return (
      <div>
        <div className="crd-landing-popup-overlay"/>
        <div className="crd-landing-popup">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-1">
                <img src="assets/logo.png"/>
              </div>
              <div className="col-sm-11">
                <h2>Corruption Risk Dashboard</h2>
              </div>
            </div>
          </div>

        </div>
      </div>
    )
  }
}

export default LandingPopup;
