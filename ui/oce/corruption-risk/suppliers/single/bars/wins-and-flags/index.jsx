import { BarChart, Bar, XAxis, YAxis, LabelList, ResponsiveContainer, Legend, Tooltip, Text } from 'recharts';
import translatable from '../../../../../translatable';
import Popup from './popup';
import { renderTopLeftLabel } from '../../../../archive/tools';

class WinsAndFlags extends translatable(React.PureComponent) {
  constructor(props) {
    super(props);
    this.state = this.state || {};
    this.state.data = [];
    this.state.length = 5;
  }

  componentDidMount() {
    const { zoomed, data, length } = this.props;
    const name = zoomed ? 'ZoomedWinsAndFlagsChart' : 'WinsAndFlagsChart';
    data.addListener(name, () => {
      data.getState(name).then(data => {
        this.setState({
          data
        })
      })
    });
    length.addListener(name, () => {
      length.getState(name).then(length => { this.setState({ length }) });
    })
  }

  componentWillUnmount() {
    const { zoomed, data, length } = this.props;
    const name = zoomed ? 'ZoomedWinsAndFlagsChart' : 'WinsAndFlagsChart';
    data.removeListener(name);
    length.removeListener(name);
  }

  render() {
    const { translations, zoomed } = this.props;
    let { data, length } = this.state;

    let height = 350;
    let slicedData = data;
    if (zoomed) {
      height = Math.max(height, data.length * 50);
    } else {
      slicedData = data.slice(0, length);
      if (slicedData.length < length) {
        for(let counter = slicedData.length; counter < length; counter++) {
          slicedData.unshift({});
        }
      }

      height = Math.max(length * 70, 200);
    }

    return (
      <div className="oce-chart">
        <ResponsiveContainer width="100%" height={height}>
          <BarChart
            layout="vertical"
            data={slicedData}
            barSize={zoomed ? 5 : 10}
            barGap={0}
            barCategoryGap={15}
          >
            <XAxis type="number" />
            <YAxis type="category" hide dataKey="name" />
            <Tooltip content={<Popup />} translations={translations} cursor={false}/>
            <Legend
              align="left"
              verticalAlign="top"
              height={30}
              iconType="square"
            />
            <Bar
              name={this.t('crd:suppliers:wins')}
              dataKey="wins"
              fill="#289df4"
              minPointSize={3}
              isAnimationActive={false}
            >
              <LabelList
                dataKey="name"
                position="insideTopLeft"
                content={renderTopLeftLabel}
              />
            </Bar>
            <Bar
              name={this.t('crd:contracts:baseInfo:flag:pl')}
              dataKey="flags"
              fill="#ce4747"
              minPointSize={3}
            />
          </BarChart>
        </ResponsiveContainer>
        {!data.length && <div className="message">No data</div>}
      </div>
    )
  }
}

export default WinsAndFlags;
