import translatable from "./translatable";
import Component from "./pure-render-component";
import {callFunc} from "./tools";
import {fromJS} from "immutable";
import URI from "urijs";
const API_ROOT = '/api';

let fetchEP = url => fetch(url.clone().query(""), {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
  },
  body: url.query()
}).then(callFunc('json'));

class Visualization extends translatable(Component){
  buildUrl(ep){
    let {filters} = this.props;
    return new URI(API_ROOT + '/' + ep).addSearch(filters.toJS());
  }

  fetch(){
    let {endpoint, endpoints} = this.constructor;
    let {requestNewData} = this.props;
    let promise = false;
    if(endpoint) promise = fetchEP(this.buildUrl(endpoint));
    if(endpoints) promise = Promise.all(endpoints.map(this.buildUrl.bind(this)).map(fetchEP));
    if(!promise) return;
    promise.then(this.transform).then(fromJS).then(data => requestNewData([], data));
  }

  transform(data){return data;}

  getData(){return this.props.data}

  componentDidMount(){
    this.fetch();
  }

  componentDidUpdate(prevProps){
    if(this.props.filters != prevProps.filters) this.fetch();
  }
}

Visualization.comparable = true;

Visualization.propTypes = {
  filters: React.PropTypes.object.isRequired,
  data: React.PropTypes.object,
  requestNewData: React.PropTypes.func.isRequired
};

export default Visualization;