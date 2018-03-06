import { ResponsiveContainer, BarChart, XAxis, YAxis, Legend, Bar, LabelList, Tooltip } from 'recharts';
import { pluck } from '../../../../tools';
import translatable from '../../../../../translatable';
import { renderTopLeftLabel } from '../tools';
import { flaggedNrData } from '../../../../../state/oce-state';
import Popup from './popup';

const POPUP_WIDTH = 350;
const POPUP_HEIGHT = 55;
const POPUP_ARROW_SIZE = 8;

const corruptionTypeColors = {
  FRAUD:  '#299df4',
  RIGGING: '#3372b2',
  COLLUSION: '#fbc42c'
}

function mkGradient(id, colors) {
  const stops = [];
  const step = 100 / colors.length;
  colors.forEach((color, index) => {
    stops.push({
      color,
      offset: step * index + 1,
    });
    stops.push({
      color,
      offset: step * (index + 1) - 1,
    });
  });

  return (
    <linearGradient id={id} x1="0%" y1="0%" x2="100%" y2="100%">
      {stops.map(({color, offset}) =>
        <stop
          offset={offset+'%'}
          style={{
            stopColor: color,
            stopOpacity: 1,
          }}
        />
      )}
    </linearGradient>
  )
}

class TaggedBar extends translatable(Bar) {
  maybeGetGradients(types) {
    return mkGradient(
      this.getGradientId(types),
      types.map(type => corruptionTypeColors[type])
    )
  }

  getGradientId(types) {
    return `gradient_${types.join('_')}`
  }

  getFill(types) {
    if (types.length === 1) {
      const [type] = types;
      return corruptionTypeColors[type];
    } else {
      return `url(#${this.getGradientId(types)})`
    }
  }

  renderRectangle(option, props) {
    const types = props.types;
    return (
      <g>
        {this.maybeGetGradients(types)}
        {super.renderRectangle(option, {
           ...props,
           fill: this.getFill(types),
        })}
      </g>
    )
  }
}

export default class FlaggedNr extends translatable(React.PureComponent) {
  constructor(props) {
    super(props);
    this.state = this.state || {};
    this.state.data = [];
  }

  componentDidMount() {
    const { zoomed } = this.props;
    const name = zoomed ? 'ZoomedFlaggedNrChart' : 'FlaggedNrChart';
    flaggedNrData.addListener(name, () => {
      flaggedNrData.getState(name).then(data => {
        this.setState({
          data
        })
      })
    })
  }

  componentWillUnmount() {
    const { zoomed } = this.props;
    const name = zoomed ? 'ZoomedFlaggedNrChart' : 'FlaggedNrChart';
    flaggedNrData.removeListener(name);

  }
  render() {
    const { zoomed, translations } = this.props;
    let { data } = this.state;
    let height = 350;
    if (zoomed) {
      height = Math.max(height, data.length * 50);
    } else {
      data = data.slice(0, 5);
      if (data.length < 5) {
        for(let counter = data.length; counter < 5; counter++) {
          data.unshift({ types: [] });
        }
      }
    }

    const corruptionTypes = new Set();
    data.forEach(
      datum => datum.types.forEach(
        type => corruptionTypes.add(type)
      )
    );

    const legendPayload = [...corruptionTypes].map(
      corruptionType => ({
        value: this.t(`crd:corruptionType:${corruptionType}:name`),
        type: 'rect',
        color: corruptionTypeColors[corruptionType],
      })
    );
    return (
      <ResponsiveContainer width="100%" height={height}>
        <BarChart
          layout="vertical"
          data={data}
          barSize={zoomed ? 10 : 20}
          barGap={0}
          barCategoryGap={15}
        >
          <XAxis type="number" />
          <YAxis type="category" dataKey="indicatorId" hide />
          <Tooltip content={<Popup />} translations={translations} cursor={false} />
          <Legend
            align="right"
            verticalAlign="top"
            payload={legendPayload}
            height={30}
          />
          <TaggedBar
            dataKey="count"
            minPointSize={3}
            isAnimationActive={false}
          >
            <LabelList
              formatter={indicatorId => this.t(`crd:indicators:${indicatorId}:name`)}
              dataKey="indicatorId"
              position="insideTopLeft"
              content={renderTopLeftLabel}
            />
          </TaggedBar>
        </BarChart>
      </ResponsiveContainer>
    )
  }
}

