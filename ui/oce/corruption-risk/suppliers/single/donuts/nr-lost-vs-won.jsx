import Donut from '../../../donut';

class CenterText extends React.Component {
  render() {
    const { data } = this.props;
    const [fst, snd] = data;
    const sum = fst + snd;
    const percent = (fst / sum) * 100;
    return (
      <div className="center-text two-rows">
        <div>
          {fst}
          <div className="secondary">
            of {sum} ({Math.trunc(percent)}%)
          </div>
        </div>
      </div>
    );
  }
}

class NrWonVsLost extends React.PureComponent {
  render() {
    return (
      <Donut
        {...this.props}
        CenterText={CenterText}
        title="# and % Contracts"
        subtitle="Won vs Lost"
        values={[{
            color: '#165781',
            label: 'Won',
        }, {
            color: '#5fa0c9',
            label: 'Lost',
        }]}
      />
    );
  }
}

export default NrWonVsLost;
