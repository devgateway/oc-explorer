import ReactDOM from 'react-dom';
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
  fixYLabels() {
    const { data } = this.props;
    if (!data.length) return;
    const deltaY = 0;
    const $this = ReactDOM.findDOMNode(this);
    const barHeight = $this.querySelector('.trace.bars .point').getBoundingClientRect().height;

    $this.querySelectorAll('.ytick').forEach(label => {
      const { width } = label.getBoundingClientRect();
      label.setAttribute('transform', `translate(${width}, ${-barHeight - deltaY})`)

      if (navigator.userAgent.indexOf('Firefox') === -1) {
        setTimeout(function() {
          const { width } = label.getBoundingClientRect();
          label.setAttribute('transform', `translate(${width + 5}, ${-barHeight - deltaY})`)
        })
      }
    });
  }

  render() {
    const { width, tags, data } = this.props;
    const fstTag = Object.keys(tags)[0];
    const y = data.map(pluck('y'));
    const dataSize = data.length;
    const plotlyData = {};
    const gradients = {};
    let style = '';

    Object.keys(tags).map(slug => {
      const { color, name } = tags[slug];
      plotlyData[slug] = {
        x: [0],
        y,
        name,
        type: 'bar',
        marker: {
          color,
        },
        hoverinfo: 'none',
        orientation: 'h'
      }
    });

    data.forEach((datum, index) => {
      plotlyData[fstTag].x[index] = datum.x;
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
            height: 350,
            margin: {t: 0, r: 20, b: 30, l: 20, pad: 0},
            paper_bgcolor: 'rgba(0, 0, 0, 0)',
            plot_bgcolor: 'rgba(0, 0, 0, 0)',
            legend: {
              xanchor: 'right',
              yanchor: 'top',
              x: .9,
              y: 1.5,
              orientation: 'h',
            },
            barmode: 'stack',
            bargap: .5
          }}
          onUpdate={this.fixYLabels.bind(this)}
        />
      </div>
    );
  }
}

export default TaggedBarChart;
