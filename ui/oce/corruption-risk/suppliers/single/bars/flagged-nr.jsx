import { ResponsiveContainer, BarChart, XAxis, YAxis, Legend, Bar, LabelList } from 'recharts';
import DataFetcher from '../../../data-fetcher';
import TaggedBarChart from '../../../tagged-bar-chart';
import { wireProps } from '../../../tools';
import { pluck, cacheFn } from '../../../../tools';
import translatable from '../../../../translatable';
import CustomPopup from '../../../custom-popup';
import BackendDateFilterable from '../../../backend-date-filterable';
import { renderTopLeftLabel } from './tools';
import { flaggedNrData } from '../../../../state/oce-state';

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
    const { zoomed } = this.props;
    let { data } = this.state;
    let height = 350;
    if (zoomed) {
      height = Math.max(height, data.length * 50);
    } else {
      data = data.slice(0, 5);
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

/* class Popup extends translatable(React.PureComponent) {
 *   render() {
 *     const { x, y, points } = this.props;
 *     const point = points[0];
 *     const { xaxis, yaxis } = point;
 *     const markerLeft = xaxis.l2p(point.x) + xaxis._offset;
 *     const markerTop = yaxis.l2p(point.pointNumber) + yaxis._offset;
 * 
 *     const left = (markerLeft / 2) - (POPUP_WIDTH / 2);
 *     const top = markerTop - POPUP_HEIGHT - (POPUP_ARROW_SIZE * 1.5);
 * 
 *     const style = {
 *       left,
 *       top,
 *       width: POPUP_WIDTH,
 *       height: POPUP_HEIGHT
 *     };
 * 
 *     const flags = point.x;
 * 
 *     const label = flags === 1 ?
 *       this.t('crd:supplier:flaggedNr:popup:sg') :
 *       this.t('crd:supplier:flaggedNr:popup:pl');
 * 
 *     return (
 *       <div
 *         className="crd-popup donut-popup text-center"
 *         style={style}
 *       >
 *         {label.replace('$#$', flags).replace('$#$', point.y)}
 *         <div className="arrow" />
 *       </div>
 *     )
 *   }
 * }
 * 
 * class FlaggedNr extends translatable(React.PureComponent) {
 *   render() {
 *     const { width, data } = this.props;
 *     return (
 *       <TaggedBarChart
 *         data={data}
 *         width={width}
 *         tags={{
 *         }}
 *       />
 *     );
 *   }
 * }
 * 
 * class FlaggedNrWrapper extends translatable(React.PureComponent) {
 *   constructor(...args){
 *     super(...args);
 *     this.getEndpoints = cacheFn(indicatorTypesMapping =>
 *       Object.keys(indicatorTypesMapping).map(id => `flags/${id}/count`)
 *     );
 *   }
 * 
 *   onRequestNewData(path, data) {
 *     const { indicatorTypesMapping } = this.props;
 *     const chartData = [];
 *     if (Array.isArray(data)) {
 *       Object.keys(indicatorTypesMapping).forEach((id, index) => {
 *         if(data[index].length) {
 *           chartData.push({
 *             x: data[index][0].count,
 *             y: this.t(``),
 *             tags: indicatorTypesMapping[id].types
 *           });
 *         }
 *       });
 *       chartData.sort((a, b) => b.x - a.x);
 *     }
 *     this.props.requestNewData(path, chartData);
 *   }
 * 
 *   render() {
 *     const { requestNewData, indicatorTypesMapping } = this.props;
 *     if (!requestNewData) return null;
 *     const endpoints = this.getEndpoints(indicatorTypesMapping);
 *     return (
 *       <BackendDateFilterable
 *         {...this.props}
 *         requestNewData={this.onRequestNewData.bind(this)}
 *       >
 *         <DataFetcher
 *           endpoints={endpoints}
 *         >
 *           <CustomPopup
 *             {...this.props}
 *             Chart={FlaggedNr}
 *             Popup={Popup}
 *           />
 *         </DataFetcher>
 *       </BackendDateFilterable>
 *     )
 *   }
 * }
 * export default FlaggedNrWrapper;*/
