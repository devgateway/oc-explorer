import FilterBox from "./box";
import TenderPrice from "../../filters/tender-price";
import AwardValue from "../../filters/award-value";

class ValueAmount extends FilterBox{
  getTitle(){
    return 'Value amount';
  }

  renderChild(slug, Component){
    const {state, onUpdate, translations} = this.props;
    const minValue = state.get('min'+slug);
    const maxValue = state.get('max'+slug);
    return <Component
               translations={translations}
               minValue={minValue}
               maxValue={maxValue}
               onUpdate={({min, max}) => {
                   minValue != min && onUpdate("min"+slug, min);
                   maxValue != max && onUpdate("max"+slug, max);
                 }}
           />
  }

  getBox(){
    return (
      <div>
        {this.renderChild("TenderValue", TenderPrice)}
        {this.renderChild("AwardValue", AwardValue)}
      </div>
    )
  }
}

export default ValueAmount;
