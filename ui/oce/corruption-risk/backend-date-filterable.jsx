import { Map, Set } from 'immutable';
import { cloneChild } from './tools';

class BackendDateFilterable extends React.PureComponent {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.decoratedFilters = this.decorateFilters(this.props);
  }

  decorateFilters({ filters, years, months }) {
    const monthly = years.count() === 1;
    return filters
      .set('year', years)
      .set('monthly', monthly)
      .set('month', monthly && months.count() != 12 ? months : Set())
    ;
  }

  componentWillUpdate(nextProps) {
    if (['filters', 'years', 'months'].some(prop => this.props[prop] != nextProps[prop])) {
      this.setState({
        decoratedFilters: this.decorateFilters(nextProps),
      })
    }
  }

  render() {
    const decoratedProps = Object.assign({}, this.props, {
      filters: this.state.decoratedFilters,
    });
    delete decoratedProps.years;
    delete decoratedProps.months;
    delete decoratedProps.monthly;
    return cloneChild(this, decoratedProps);
  }
}

export default BackendDateFilterable;
