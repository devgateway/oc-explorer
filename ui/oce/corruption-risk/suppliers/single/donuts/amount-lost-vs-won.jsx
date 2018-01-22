import { pluck } from '../../../../tools';
import Donut from '../../../donut';

const BILLION = 1000000000;
const MILLION = 1000000;
const THOUSAND = 1000;

const formatNumber = number => number.toLocaleString(undefined, {maximumFractionDigits: 2});

const format = number => {
  if(typeof number == "undefined") return number;
  let abs = Math.abs(number);
  if(abs >= BILLION) return [formatNumber(number/BILLION), <span className="multiplier">B</span>];
  if(abs >= MILLION) return [formatNumber(number/MILLION), <span className="multiplier">M</span>];
  if(abs >= THOUSAND) return [formatNumber(number/THOUSAND), <span className="multiplier">K</span>];
  return formatNumber(number);
};

class CenterText extends React.PureComponent {
  render() {
    const { data, styling } = this.props;
    const [fst, snd] = data.map(pluck('value'));
    return (
      <div className="center-text two-rows">
        <div>
          ${format(fst)}
          <div className="secondary">
            ${snd} Lost
          </div>
        </div>
      </div>
    );
  }
}

class AmountWonVsLost extends React.Component {
  transformNewData(path, data) {
    this.props.requestNewData(path, [{
      color: '#2e833a',
      label: 'Won',
      value: data.getIn([0, 'won', 'totalAmount']),
    }, {
      color: '#72c47e',
      label: 'Lost',
      value: data.getIn([0, 'lostAmount'])
    }]);
  }
  render() {
    const { data } = this.props;
    return (
      <Donut
        {...this.props}
        requestNewData={this.transformNewData.bind(this)}
        data={this.props.data || []}
        CenterText={CenterText}
        title="$ Amount"
        subtitle="Won vs Lost"
        endpoint="procurementsWonLost"
      />
    );
  }
}

export default AmountWonVsLost;
