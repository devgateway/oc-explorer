import Donut from '../../../donut';
import { pluck } from '../../../../tools';

class CenterText extends React.PureComponent {
  render() {
    const { data, values } = this.props;
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
            label: 'Fraud',
        }, {
            color: '#3372b2',
            label: 'Process Rigging'
        }, {
            color: '#30a0f5',
            label: 'Collusion',
        }]}
      />
    );
  }
}

export default TotalFlags;
