import { Set } from 'immutable';
import { cacheFn } from '../tools';

const frontendDateFilterable = (Class) => {
  class Filterable extends Class {
    buildUrl(...args) {
      const url = super.buildUrl(...args);
      return this.props.monthly ?
          url.addSearch('monthly', true).addSearch('year', this.props.years.toArray()) :
          url;
    }

    getData() {
      const data = super.getData();
      const { years, monthly, months } = this.props;
      if (!data) return data;
      if (monthly) {
        return this.constructor.filterDataByMonth(data, months);
      } else if (years.count()) {
        return this.constructor.filterDataByYears(data, years);
      }
      return data;
    }

    componentDidUpdate(prevProps) {
      const monthlyToggled = this.props.monthly !== prevProps.monthly;
      const isMonthlyAndYearChanged = this.props.monthly && this.props.years !== prevProps.years;
      if (monthlyToggled || isMonthlyAndYearChanged) {
        this.fetch();
      } else super.componentDidUpdate(prevProps);
    }
  }

  Filterable.propTypes = Filterable.propTypes || {};
  Filterable.propTypes.years = React.PropTypes.object.isRequired;

  Filterable.computeYears = cacheFn((data) => {
    if (!data) return Set();
    return Set(data.map(datum => +datum.get('year')));
  });

  const filterDataByDate = (field, data, dates) =>
      data.filter(datum => dates.has(+datum.get(field))).sortBy(datum => +datum.get(field));

  Filterable.filterDataByMonth = cacheFn(filterDataByDate.bind(null, 'month'));

  Filterable.filterDataByYears = cacheFn(filterDataByDate.bind(null, 'year'));

  return Filterable;
};

export default frontendDateFilterable;
