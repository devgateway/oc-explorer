import URI from 'urijs';
import { Map } from 'immutable';
import { cloneChild } from './tools';
import { callFunc } from '../tools';

const API_ROOT = '/api';

const fetchEP = url => fetch(url.clone().query(''), {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
  },
  credentials: 'same-origin',
  body: url.query(),
}).then(callFunc('json'));

class DataFetcher extends React.PureComponent {
  fetch() {
    const { filters, endpoint, endpoints, requestNewData } = this.props;
    if (endpoint) {
      const uri = new URI(`${API_ROOT}/${endpoint}`).addSearch(filters.toJS());
      fetchEP(uri).then(requestNewData.bind(null, []));
    } else if (endpoints) {
      Promise.all(
        endpoints.map(endpoint =>
          fetchEP(
            new URI(`${API_ROOT}/${endpoint}`).addSearch(filters.toJS())
          )
        )
      ).then(requestNewData.bind(null, []));
    }
  }

  componentDidMount() {
    this.fetch();
  }

  componentDidUpdate(prevProps) {
    if (['filters', 'endpoint', 'endpoints'].some(prop => this.props[prop] != prevProps[prop])) {
      this.props.requestNewData([], null);
      this.fetch();
    }
  }

  render() {
    const { data } = this.props;
    if (!data) return null;
    return cloneChild(this, {
      data
    });
  }
}

DataFetcher.defaultProps = {
  filters: Map(),
  requestNewData: () => null,
}

export default DataFetcher;
