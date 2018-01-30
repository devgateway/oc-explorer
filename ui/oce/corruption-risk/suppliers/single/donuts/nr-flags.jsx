import { List } from 'immutable';
import Donut from '../../../donut';
import translatable from '../../../../translatable';

class CenterText extends React.PureComponent {
  render() {
    const { data } = this.props;
    return (
      <div className="center-text two-rows total-flags-center-text">
        {data.map(({ color, value }) =>
          <span key={color} style={{ color }}>{value}</span>
        )}
      </div>
    );
  }
}

const COLORS = ['#fbc42c', '#3372b2', '#30a0f5']

class TotalFlags extends translatable(React.PureComponent) {
  render() {
    const data = (this.props.data || List()).map((datum, index) => {
      const value = datum.get('indicatorCount');
      const indicatorName = this.t(`crd:corruptionType:${datum.get('type')}:name`);
        return {
        color: COLORS[index],
        label: `${value} ${indicatorName} ${value === 1 ? 'flag' : 'flags'}`,
        value: datum.get('indicatorCount'),
      }
    }).toJS();
    return (
      <Donut
        {...this.props}
        data={data}
        CenterText={CenterText}
        endpoint="totalFlaggedIndicatorsByIndicatorType"
        title="Total flags by risk type"
        subtitle="on all procurements won"
      />
    );
  }
}

export default TotalFlags;
