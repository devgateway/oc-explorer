import { List } from 'immutable';
import Donut from '../../../donut';
import { pluck } from '../../../../tools';
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
    const data = (this.props.data || List()) .map((datum, index) => ({
      color: COLORS[index],
      label: this.t(`crd:corruptionType:${datum.get('type')}:name`),
      value: datum.get('indicatorCount'),
    })).toJS();
    return (
      <Donut
        {...this.props}
        data={data}
        CenterText={CenterText}
        endpoint='totalFlaggedIndicatorsByIndicatorType'
        title="Total Flags"
        subtitle="by Risk Type"
      />
    );
  }
}

export default TotalFlags;
