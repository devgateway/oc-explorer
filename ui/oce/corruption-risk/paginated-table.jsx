import URI from 'urijs';
import Visualization from '../visualization';

class PaginatedTable extends Visualization {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.pageSize = 20;
    this.state.page = 1;
  }

  getCustomEP() {
    const { pageSize, page } = this.state;
    const { dataEP, countEP } = this.props;

    let data = new URI(dataEP)
      .addSearch('pageSize', pageSize)
      .addSearch('pageNumber', page - 1);

    let count = new URI(countEP);

    return [
      data,
      count,
    ];
  }

  transform([data, count]) {
    return {
      data,
      count,
    };
  }

  componentDidUpdate(_, prevState) {
    const stateChanged = ['pageSize', 'page'].some(key => this.state[key] !== prevState[key]);
    if (stateChanged) {
      this.fetch();
    }
  }

  render() {
    throw 'Abstract!';
  }
}

export default PaginatedTable;
