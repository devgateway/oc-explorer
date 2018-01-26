import { pluck } from '../tools';
import PlotlyChart from './plotly-chart';

function mkGradient(id, colors) {
  const stops = [];
  const step = 100 / colors.length;
  colors.forEach((color, index) => {
    stops.push({
      color,
      offset: step * index,
    });
    stops.push({
      color,
      offset: step * (index + 1),
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

class TaggedBarChart extends React.PureComponent {
  render() {
    const { width, tags, data } = this.props;
    const fstTag = Object.keys(tags)[0];
    const x = data.map(pluck('x'));
    const dataSize = data.length;
    const plotlyData = {};
    const gradients = {};
    let style = '';

    Object.keys(tags).map(slug => {
      const { color, name } = tags[slug];
      plotlyData[slug] = {
        x,
        y: [0],
        name,
        type: 'bar',
        marker: {
          color,
        },
        hoverinfo: 'none',
      }
    });

    plotlyData[fstTag].width = Array(dataSize).fill(.5);

    data.forEach((datum, index) => {
      plotlyData[fstTag].y[index] = datum.y;
      if (datum.tags.length > 1) {
        const gradientSlug = datum.tags.join('_');
        gradients[gradientSlug] = gradients[gradientSlug] ||
          datum.tags.map(tag => tags[tag].color);
        style += `#tagged-bar-chart .point:nth-child(${index + 1}) path {` +
          `fill: url(#${gradientSlug})!important;}\n`;
      }
    });

    return (
      <div id="tagged-bar-chart">
        <svg width="1" height="1" style={{float: 'right'}}>
          <defs>
            {Object.entries(gradients).map(([slug, colors]) => mkGradient(slug, colors))}
          </defs>
        </svg>
        <style dangerouslySetInnerHTML={{__html: style}}/>
        <PlotlyChart
          data={Object.values(plotlyData)}
          layout={{
            width,
            height: 250,
            barmode: 'stack',
            margin: {t: 0, r: 20, b: 80, l: 20, pad: 0},
            paper_bgcolor: 'rgba(0, 0, 0, 0)',
            plot_bgcolor: 'rgba(0, 0, 0, 0)',
            legend: {
              xanchor: 'right',
              yanchor: 'top',
              x: .9,
              y: 1.5,
              orientation: 'h',
            },
            xaxis: {
              tickangle: 25
            }
          }}
        />
      </div>
    );
  }
}

export default TaggedBarChart;
