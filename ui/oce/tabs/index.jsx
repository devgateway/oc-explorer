import DataFetcher from "../data-fetcher";
import {Set, List} from "immutable";
import {Map} from "immutable";
import DefaultComparison from "../comparison";

class Tab extends DataFetcher{
  maybeWrap({dontWrap, getName}, index, rendered){
    return dontWrap ? rendered : <section key={index}>
      <h4 className="page-header">{getName(this.__.bind(this))}</h4>
      {rendered}
    </section>
  }

  compare(Component, index){
    let {compareBy, comparisonData, comparisonCriteriaValues, filters, requestNewComparisonData, years, bidTypes
        , width, translations} = this.props;
    let {compareWith: CustomComparison} = Component;
    let Comparison = CustomComparison || DefaultComparison;
    return <Comparison
        key={Component}
        compareBy={compareBy}
        comparisonData={comparisonData.get(index, List())}
        comparisonCriteriaValues={comparisonCriteriaValues}
        filters={filters}
        requestNewComparisonData={(path, data) => requestNewComparisonData([index, ...path], data)}
        years={years}
        Component={Component}
        bidTypes={bidTypes}
        width={width}
        translations={translations}
    />
  }

  render(){
    let {filters, compareBy, requestNewData, data, years, width, translations} = this.props;
    return <div className="col-sm-12 content">
        {this.constructor.visualizations.map((Component, index) =>
            compareBy && Component.comparable ? this.compare(Component, index) :
            this.maybeWrap(Component, index,
              <Component
                key={index}
                filters={filters}
                requestNewData={(_, data) => requestNewData([index], data)}
                data={data.get(index)}
                years={years}
                width={width}
                translations={translations}
              />
            )
        )}
    </div>
  }

  static computeYears(data){
    if(!data) return Set();
    return this.visualizations.reduce((years, visualization, index) =>
        visualization.computeYears ?
            years.union(visualization.computeYears(data.get(index))) :
            years
    , Set())
  }

  static computeComparisonYears(data){
    if(!data) return Set();
    return this.visualizations.reduce((years, visualization, index) =>
      years.union(
          data.get(index, List()).reduce((years, data, index) =>
            visualization.computeYears ?
                years.union(visualization.computeYears(data)) :
                years
        , Set())
      )
    , Set())
  }
}

Tab.visualizations = [];

Tab.propTypes = {
  filters: React.PropTypes.object.isRequired,
  data: React.PropTypes.object,
  comparisonData: React.PropTypes.object,
  requestNewData: React.PropTypes.func.isRequired,
  requestNewComparisonData: React.PropTypes.func.isRequired,
  compareBy: React.PropTypes.string.isRequired,
  comparisonCriteriaValues: React.PropTypes.arrayOf(React.PropTypes.string).isRequired,
  width: React.PropTypes.number.isRequired
};


export default Tab;