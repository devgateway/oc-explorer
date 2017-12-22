import { LOGIN_URL } from "./constants";
import { debounce } from "../tools";
import translatable from '../translatable';

class LandingPopup extends translatable(React.Component) {
  constructor(...args){
    super(...args);
    this.state = {
      top: 0
    }
  }

  onClose(){
    const {redirectToLogin, requestClosing} = this.props;
    if(redirectToLogin){
      const hash = encodeURIComponent(location.hash);
      location.href = `${LOGIN_URL}${hash}`;
    } else {
      requestClosing();
    }
  }

  recalcTop(){
    this.setState({
      top: (window.innerHeight - this.refs.thePopup.offsetHeight) / 2
    })
  }

  componentDidMount(){
    this.recalcTop();
    this.windowResizeListener = debounce(this.recalcTop.bind(this));
    window.addEventListener("resize", this.windowResizeListener);
  }

  componentWillUnmount(){
    window.removeEventListener("resize", this.windowResizeListener);
  }

  render(){
    const { top } = this.state;
    const { languageSwitcher } = this.props;
    return (
      <div>
        <div className="crd-landing-popup-overlay" onClick={this.onClose.bind(this)}/>

        <div className="crd-landing-popup" ref="thePopup" style={{top}}>
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-1 text-right">
                <img src="assets/logo.png"/>
              </div>
              <div className="col-sm-9">
                <h4 className="popup-title">{this.t('crd:title')}</h4>
              </div>
              <div className="col-sm-1 language-switcher">
                {languageSwitcher()}
              </div>
              <div className="col-sm-1 text-right">
                <i className="glyphicon glyphicon-remove-circle close-button" onClick={this.onClose.bind(this)}></i>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-5 col-sm-offset-1 text-column-left">
                {this.t('crd:landing:introduction:1')}
              </div>
              <div
                className="col-sm-5 end text-column-right"
                dangerouslySetInnerHTML={{__html: this.t('crd:landing:introduction:2')}}
              />
              <div className="col-sm-1"/>

              <hr className="col-sm-offset-1 col-sm-10 end separator"/>
              <div className="col-sm-1"/>

            </div>
            <div className="row">
              <div className="col-sm-offset-1 col-sm-11 section-title">
                <h5>
                  {this.t('crd:landing:intendedUse:title')}
                </h5>
              </div>

              <div className="col-sm-11 col-sm-offset-1">
                {this.t('crd:landing:intendedUse:introduction')}
              </div>
              <div className="col-sm-5 col-sm-offset-1 text-column-left intended-use-item">
                {this.t('crd:landing:intendedUse:introduction:1')}
              </div>

              <div className="col-sm-5 text-column-right intended-use-item">
                {this.t('crd:landing:intendedUse:introduction:2')}
              </div>
            </div>
            <div className="row">

              <div className="col-sm-1"/>

              <hr className="col-sm-offset-1 col-sm-10 end separator"/>

              <div className="col-sm-offset-1 col-sm-11 section-title">
                <h5
                  dangerouslySetInnerHTML={{__html: this.t('crd:landing:distinguishing:title')}}
                />
              </div>

              <div className="col-sm-5 col-sm-offset-1 text-column-left">
                {this.t('crd:landing:distinguishing:1')}
              </div>
              <div className="col-sm-5 end text-column-right">
                {this.t('crd:landing:distinguishing:2')}
              </div>

              <hr className="col-sm-offset-1 col-sm-10 end separator"/>

              <div className="col-sm-11 col-sm-offset-1 section-title">
                <h5>{this.t('crd:landing:feedback:title')}</h5>
              </div>

              <div
                className="col-sm-9 col-sm-offset-1 contact"
                dangerouslySetInnerHTML={{__html: this.t('crd:landing:feedback:text')}}
              />

              <div className="col-sm-2 end">
                <button className="btn btn-primary" onClick={this.onClose.bind(this)}>
                  {this.t('crd:landing:enter')}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default LandingPopup;
