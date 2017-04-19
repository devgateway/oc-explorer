import FilterBox from "./box";
import TenderPrice from "../../filters/tender-price";
import AwardValue from "../../filters/award-value";

class ValueAmount extends FilterBox {
  getTitle() {
    return 'Value amount';
  }

  update(slug, {min, max}, {min: minPossibleValue, max: maxPossibleValue}) {
    const {state, onUpdate} = this.props;
    const minValue = state.get('min' + slug) || minPossibleValue;
    const maxValue = state.get('max' + slug) || maxPossibleValue;
    if(minValue != min) {
      onUpdate("min" + slug, min == minPossibleValue ? undefined : min)
    }else if(maxValue != max) {
      onUpdate("max" + slug, max == maxPossibleValue ? undefined : max)
    }
  }

  renderChild(slug, Component) {
    const {state, translations} = this.props;
    const minValue = state.get('min' + slug);
    const maxValue = state.get('max' + slug);
    return (
        <Component
            translations={translations}
            minValue={minValue}
            maxValue={maxValue}
            onUpdate={this.update.bind(this, slug)}
        />
    )
  }

  getBox() {
    return (
        <div className="box-content">
          {this.renderChild("TenderValue", TenderPrice)}
          {this.renderChild("AwardValue", AwardValue)}
        </div>
    )
  }
}

export default ValueAmount;
