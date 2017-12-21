import Donut from '../../../donut';
import { pluck } from '../../../../tools';

class CenterText extends React.PureComponent {
  render() {
    const { data, values } = this.props;
    console.log(values);
    const [fst, snd, thrd] = data;
    const [fstColor, sndColor, thrdColor] = values.map(pluck('color'));
    return (
      <div className="center-text two-rows">
        <span style={{ color: fstColor }}>{fst}</span>/
        <span style={{ color: sndColor }}>{snd}</span>/
        <span style={{ color: thrdColor }}>{thrd}</span>
      </div>
    );
  }
}

class TotalFlags extends React.Component {
  render() {
    return (
      <Donut
        {...this.props}
        CenterText={CenterText}
        title="Total Flags"
        subtitle="by Risk Type"
        values={[{
            color: '#fbc42c',
        }, {
            color: '#3372b2',
        }, {
            color: '#30a0f5',
        }]}
      />
    );
  }
}

export default TotalFlags;
