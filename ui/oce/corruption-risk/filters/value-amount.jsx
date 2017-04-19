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
    return (
			<Component
			translations={translations}
			minValue={minValue}
			maxValue={maxValue}
			onUpdate={({min, max}, {min: minPossibleValue, max: maxPossibleValue}) => {
				if(minValue != min){
					onUpdate("min"+slug, min == minPossibleValue ? "" : min)
				}
				if(maxValue != max){
					onUpdate("max"+slug, max == maxPossibleValue ? "" : max)
				}
			}}
			/>
		)
  }

  getBox(){
    return (
      <div className="box-content">
        {this.renderChild("TenderValue", TenderPrice)}
        {this.renderChild("AwardValue", AwardValue)}
      </div>
    )
  }
}

export default ValueAmount;
