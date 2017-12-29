import { List } from 'immutable';
import Donut from '../../../donut';
import { pluck } from '../../../../tools';

class CenterText extends React.PureComponent {
  render() {
    const { data } = this.props;
    const [fst, snd, thrd] = data.map(pluck('value'));
    const [fstColor, sndColor, thrdColor] = data.map(pluck('color'));
    return (
      <div className="center-text two-rows">
        <span style={{ color: fstColor }}>{fst}</span>/
        <span style={{ color: sndColor }}>{snd}</span>/
        <span style={{ color: thrdColor }}>{thrd}</span>
      </div>
    );
  }
}

const COLORS = ['#fbc42c', '#3372b2', '#30a0f5']

class TotalFlags extends React.Component {
  render() {
    const data = (this.props.data || List()) .map((datum, index) => ({
      color: COLORS[index],
      label: datum.get('type'),
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
