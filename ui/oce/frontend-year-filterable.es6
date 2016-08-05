import {Set} from "immutable";
import {pluckImm, cacheFn} from "./tools";

let frontendYearFilterable = Class => {
  class Filterable extends Class{
    getData(){
      let data = super.getData();
      let {years} = this.props;
      if(!data || !years) return data;
      return this.constructor.filterDataByYears(data, years);
    }
  }

  Filterable.propTypes = Filterable.propTypes || {};
  Filterable.propTypes.years = React.PropTypes.object.isRequired;

  Filterable.computeYears = cacheFn(data => {
    if(!data) return Set();
    return Set(data.map(datum => {
      return +datum.get('year')
    }));
  });

  Filterable.filterDataByYears = cacheFn((data, years) =>
      data.filter(datum => years.has(+datum.get('year'))).sortBy(pluckImm('year'))
  );

  return Filterable;
};

export default frontendYearFilterable;
