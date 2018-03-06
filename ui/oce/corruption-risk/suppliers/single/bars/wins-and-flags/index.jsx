import { BarChart, Bar, XAxis, YAxis, LabelList, ResponsiveContainer, Legend, Tooltip } from 'recharts';
import translatable from '../../../../../translatable';
import Popup from './popup';
import { winsAndFlagsData } from '../../../../../state/oce-state';
import { renderTopLeftLabel } from '../tools';

class WinsAndFlags extends translatable(React.PureComponent) {
  constructor(props) {
    super(props);
    this.state = this.state || {};
    this.state.data = [];
  }

  componentDidMount() {
    const { zoomed } = this.props;
    const name = zoomed ? 'ZoomedWinsAndFlagsChart' : 'WinsAndFlagsChart';
    winsAndFlagsData.addListener(name, () => {
      winsAndFlagsData.getState(name).then(data => {
        this.setState({
          data
        })
      })
    })
  }

  componentWillUnmount() {
    const { zoomed } = this.props;
    const name = zoomed ? 'ZoomedWinsAndFlagsChart' : 'WinsAndFlagsChart';
    winsAndFlagsData.removeListener(name);
  }

  render() {
    const { translations, zoomed } = this.props;
    let { data } = this.state;

    let height = 350;
    if (zoomed) {
      height = Math.max(height, data.length * 50);
    } else {
      data = data.slice(0, 5);
      if (data.length < 5) {
        for(let counter = data.length; counter < 5; counter++) {
          data.unshift({});
        }
      }
    }

    return (
      <ResponsiveContainer width="100%" height={height}>
        <BarChart
          layout="vertical"
          data={data}
          barSize={zoomed ? 5 : 10}
          barGap={0}
          barCategoryGap={15}
        >
          <XAxis type="number" />
          <YAxis type="category" hide dataKey="PEName" />
          <Tooltip content={<Popup />} translations={translations} cursor={false}/>
          <Legend
            align="right"
            verticalAlign="top"
            height={30}
          />
          <Bar
            name={this.t('crd:suppliers:wins')}
            dataKey="wins"
            fill="#289df4"
            minPointSize={3}
            isAnimationActive={false}
          >
            <LabelList
              dataKey="PEName"
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
    )
  }
}

export default WinsAndFlags;
