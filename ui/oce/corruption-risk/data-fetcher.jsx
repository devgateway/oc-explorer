import URI from 'urijs';
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

const cloneChild = (component, props) =>
  React.cloneElement(
    React.Children.only(component.props.children),
    props
  );

class DataFetcher extends React.PureComponent {
  fetch() {
    const { filters, endpoint, requestNewData } = this.props;
    const uri = new URI(`${API_ROOT}/${endpoint}`).addSearch(filters.toJS());
    fetchEP(uri).then(requestNewData.bind(null, []));
  }

  componentDidMount() {
    this.fetch();
  }

  componentDidUpdate(prevProps) {
    if (['filters', 'endpoint'].some(prop => this.props[prop] != prevProps[prop])) {
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

export default DataFetcher;
