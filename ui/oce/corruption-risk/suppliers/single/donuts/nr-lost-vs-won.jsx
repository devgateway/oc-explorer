import { fromJS } from 'immutable';
import { pluck } from '../../../../tools';
import Donut from '../../../donut';

class CenterText extends React.Component {
  render() {
    const { data } = this.props;
    if (!data) return null;
    const [won, lost] = data.map(pluck('value'));
    const sum = won + lost;
    const percent = (won / sum) * 100;
    return (
      <div className="center-text two-rows">
        <div>
          {won}
          <div className="secondary">
            of {sum} ({Math.trunc(percent)}%)
          </div>
        </div>
      </div>
    );
  }
}

class NrWonVsLost extends React.PureComponent {
  transformNewData(path, data) {
    this.props.requestNewData(path, [{
      color: '#165781',
      label: 'Won',
      value: data.getIn([0, 'won', 'count']),
    }, {
      color: '#5fa0c9',
      label: 'Lost',
      value: data.getIn([0, 'lostCount'])
    }]);
  }

  render() {
    return (
      <Donut
        {...this.props}
        requestNewData={this.transformNewData.bind(this)}
        data={this.props.data || []}
        CenterText={CenterText}
        title="No. & Percent of Procurements"
        subtitle="Won vs. Lost"
        endpoint="procurementsWonLost"
      />
    );
  }
}

export default NrWonVsLost;
