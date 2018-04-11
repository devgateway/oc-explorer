import { BarChart, Bar, XAxis, YAxis, LabelList, ResponsiveContainer, Legend, Tooltip } from 'recharts';
import translatable from '../../../../../translatable';
import Popup from './popup';
import { renderTopLeftLabel } from '../../../../archive/tools';
import { maxCommonDataLength } from '../../state';

class ProcurementsByMethod extends translatable(React.PureComponent) {
  constructor(props) {
    super(props);
    this.state = this.state || {};
    this.state.data = [];
    this.state.length = 5;
  }

  componentDidMount() {
    const { zoomed, data } = this.props;
    const name = zoomed ? 'ZoomedProcurementsByMethodChart' : 'ProcurementsByMethodChart';
    data.addListener(name, () => {
      data.getState(name).then(data => {
        this.setState({
          data
        })
      })
    });
    maxCommonDataLength.addListener(name, () => {
      maxCommonDataLength.getState(name).then(length => this.setState({ length }))
    });
  }

  componentWillUnmount() {
    const { zoomed, data } = this.props;
    const name = zoomed ? 'ZoomedProcurementsByMethodChart' : 'ProcurementsByMethodChart';
    data.removeListener(name);
    maxCommonDataLength.removeListener(name);
  }

  render() {
    const { translations, zoomed } = this.props;
    let { data, length } = this.state;

    let height = 350;
    if (zoomed) {
      height = Math.max(height, data.length * 50);
    } else {
      data = data.slice(0, length);
      if (data.length < length) {
        for (let counter = data.length; counter < length; counter++) {
          data.unshift({});
        }
      }
      height = length * 70;
    }

    return (
      <div className="oce-chart">
        <ResponsiveContainer width="100%" height={height}>
          <BarChart
            layout="vertical"
            data={data}
            barSize={zoomed ? 10 : 20}
            barGap={0}
            barCategoryGap={15}
          >
            <XAxis type="number" />
            <YAxis type="category" hide dataKey="status" />
            <Tooltip content={<Popup />} translations={translations} cursor={false} />
            <Bar
              name={this.t('crd:procuringEntities:byMethod:title')}
              dataKey="count"
              fill="#289df4"
              minPointSize={3}
              isAnimationActive={false}
            >
              <LabelList
                dataKey="status"
                position="insideTopLeft"
                content={renderTopLeftLabel}
              />
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </div>
    )
  }
}

export default ProcurementsByMethod;
