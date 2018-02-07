import { fromJS } from 'immutable';
import { pluck } from '../../../../tools';
import Donut from '../../../donut';
import translatable from '../../../../translatable';

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

class NrWonVsLost extends translatable(React.PureComponent) {
  transformNewData(path, data) {
    const won = data.getIn([0, 'won', 'count']);
    const lost = data.getIn([0, 'lostCount']);
    const sum = won + lost;
    const wonPercent = (won / sum * 100).toFixed(2);
    const lostPercent = (lost / sum * 100).toFixed(2);
    this.props.requestNewData(path, [{
      color: '#165781',
      label: this.t('crd:supplier:nrLostVsWon:won').replace('$#$', won).replace('$#$', wonPercent),
      value: won
    }, {
      color: '#5fa0c9',
      label: this.t('crd:supplier:nrLostVsWon:lost').replace('$#$', lost).replace('$#$', lostPercent),
      value: lost
    }]);
  }

  render() {
    return (
      <Donut
        {...this.props}
        requestNewData={this.transformNewData.bind(this)}
        data={this.props.data || []}
        CenterText={CenterText}
        title={this.t('crd:supplier:nrLostVsWon:title')}
        subtitle={this.t('crd:supplier:nrLostVsWon:subtitle')}
        endpoint="procurementsWonLost"
      />
    );
  }
}

export default NrWonVsLost;
