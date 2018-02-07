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

const COLORS = ['#fbc42c', '#3372b2', '#30a0f5'];

class TotalFlags extends translatable(React.PureComponent) {
  render() {
    const data = (this.props.data || List()).map((datum, index) => {
      const value = datum.get('indicatorCount');
      const indicatorName = this.t(`crd:corruptionType:${datum.get('type')}:name`);
      const label = value === 1 ?
        this.t('crd:supplier:nrFlags:label:sg') :
        this.t('crd:supplier:nrFlags:label:pl');

      return {
        color: COLORS[index],
        label: label.replace('$#$', value).replace('$#$', indicatorName),
        value: datum.get('indicatorCount'),
      };
    }).toJS();
    return (
      <Donut
        {...this.props}
        data={data}
        CenterText={CenterText}
        endpoint="totalFlaggedIndicatorsByIndicatorType"
        title={this.t('crd:supplier:nrFlags:title')}
        subtitle={this.t('crd:supplier:nrFlags:subtitle')}
      />
    );
  }
}

export default TotalFlags;
