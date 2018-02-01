import { pluck } from '../../../../tools';
import Donut from '../../../donut';

class CenterText extends React.PureComponent {
  format(number) {
    const formatted = this.props.styling.charts.hoverFormatter(number) || '';
    return [
      formatted.slice(0, -1),
      <span className="multiplier">{formatted.slice(-1)}</span>
    ];
  }

  render() {
    const { data } = this.props;
    const [fst, snd] = data.map(pluck('value'));
    return (
      <div className="center-text two-rows">
        <div>
          ${this.format(fst)}
          <div className="secondary">
            ${this.format(snd)} Lost
          </div>
        </div>
      </div>
    );
  }
}

class AmountWonVsLost extends React.Component {
  transformNewData(path, data) {
    const { styling } = this.props;
    const won = data.getIn([0, 'won', 'totalAmount']);
    const lost = data.getIn([0, 'lostAmount']);

    this.props.requestNewData(path, [{
      color: '#2e833a',
      label: `$${styling.charts.hoverFormatter(won)} won`,
      value: won
    }, {
      color: '#72c47e',
      label: `$${styling.charts.hoverFormatter(lost)} lost`,
      value: lost
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
        title="Amount ($)"
        subtitle="Won vs. Lost"
        endpoint="procurementsWonLost"
      />
    );
  }
}

export default AmountWonVsLost;
