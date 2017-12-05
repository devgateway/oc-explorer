import URI from 'urijs';
import { fromJS } from 'immutable';
import translatable from './translatable';
import Component from './pure-render-component';
import { callFunc } from './tools';

const API_ROOT = '/api';

const fetchEP = url => fetch(url.clone().query(''), {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
  },
  credentials: 'same-origin',
  body: url.query(),
}).then(callFunc('json'));

class Visualization extends translatable(Component) {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state.loading = true;
  }

  buildUrl(ep) {
    const { filters } = this.props;
    return new URI(`${API_ROOT}/${ep}`).addSearch(filters.toJS());
  }

  fetch() {
    const { endpoint, endpoints } = this.constructor;
    const { requestNewData } = this.props;
    let promise = false;
    if (endpoint) {
      console.warn('endpoint property is deprecated', endpoint);
      promise = fetchEP(this.buildUrl(endpoint));
    }
    if (endpoints) {
      console.warn('endpoints property is deprecated', endpoints);
      promise = Promise.all(endpoints.map(this.buildUrl.bind(this)).map(fetchEP));
    }
    if (typeof this.getCustomEP === 'function') {
      const customEP = this.getCustomEP();
      if (Array.isArray(customEP)) {
        promise = Promise.all(customEP.map(this.buildUrl.bind(this)).map(fetchEP));
      } else {
        promise = fetchEP(this.buildUrl(customEP));
      }
    }
    if (!promise) return;
    this.setState({ loading: true });
    promise
      .then(this.transform.bind(this))
      .then(fromJS)
      .then(data => requestNewData([], data))
      .then(() => this.setState({ loading: false }));
  }

  transform(data) { return data; }

  getData() { return this.props.data; }

  componentDidMount() {
    this.fetch();
  }

  componentDidUpdate(prevProps) {
    if (this.props.filters !== prevProps.filters) this.fetch();
  }
}

Visualization.comparable = true;

Visualization.propTypes = {
  filters: React.PropTypes.object.isRequired,
  data: React.PropTypes.object,
  requestNewData: React.PropTypes.func.isRequired,
};

export default Visualization;
