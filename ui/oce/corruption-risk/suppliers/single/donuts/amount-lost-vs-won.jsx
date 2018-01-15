import { pluck } from '../../../../tools';
import Donut from '../../../donut';

class CenterText extends React.PureComponent {
  render() {
    const { data } = this.props;
    const [fst, snd] = data.map(pluck('value'));
    return (
      <div className="center-text two-rows">
        <div>
          {fst}
          <div className="secondary">
            {snd}
          </div>
        </div>
      </div>
    );
  }
}

class AmountWonVsLost extends React.Component {
  render() {
    const { data } = this.props;
    return (
      <Donut
        {...this.props}
        CenterText={CenterText}
        title="$ Amount"
        subtitle="Won vs Lost"
        data={[{
            color: '#72c47e',
            label: 'Won',
            value: 1000000,
        }, {
            color: '#2e833a',
            label: 'Lost',
            value: 2000000,
        }]}
      />
    );
  }
}

export default AmountWonVsLost;
