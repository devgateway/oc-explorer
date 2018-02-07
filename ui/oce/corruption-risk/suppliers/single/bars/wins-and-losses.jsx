import ReactDOM from 'react-dom';
import PlotlyChart from '../../../plotly-chart';
import DataFetcher from '../../../data-fetcher';
import { wireProps } from '../../../tools';
import { pluck } from '../../../../tools';
import CustomPopup from '../../../custom-popup';
import translatable from '../../../../translatable';

const POPUP_ARROW_SIZE = 8;

class Popup extends translatable(React.PureComponent) {
  render() {
    const { x, y, points } = this.props;
    const [a, b] = points;
    const point = a.x < b.x ? a : b;
    const { xaxis, yaxis } = point;
    const markerLeft = xaxis.l2p(point.x) + xaxis._offset;
    const markerTop = yaxis.l2p(point.pointNumber) + yaxis._offset;

    const PEname = points[0].y;

    let POPUP_HEIGHT = 70;
    let POPUP_WIDTH = 300;

    if (PEname.length > 100) {
      POPUP_HEIGHT = 140;
    } else if (PEname.length > 70 ) {
      POPUP_HEIGHT = 110;
    } else if (PEname.length > 40 ) {
      POPUP_HEIGHT = 90;
    }

    const left = markerLeft - (POPUP_WIDTH / 2);
    const top = markerTop - POPUP_HEIGHT - (POPUP_ARROW_SIZE * 2);

    const style = {
      left,
      top,
      width: POPUP_WIDTH,
      height: POPUP_HEIGHT
    };

    const wins = points[0].x;
    const flags = points[1].x;

    const winLabel = wins === 1 ?
      this.t('crd:supplier:win:sg') :
      this.t('crd:supplier:win:pl');

    const flagLabel = flags === 1 ?
      this.t('crd:contracts:baseInfo:flag:sg') :
      this.t('crd:contracts:baseInfo:flag:pl');

    return (
      <div
        className="crd-popup donut-popup text-center"
        style={style}
      >
        {PEname}
        <br />
        {wins} {winLabel}, {flags} {flagLabel}
        <div className="arrow" />
      </div>
    )
  }
}

class WinsBarChart extends React.PureComponent {
  fixYLabels() {
    const { data } = this.props;
    let deltaY = 40 / data[0].x.length;
    const isZoomed = data[0].x.length > 5;
    if (isZoomed) deltaY += 10;
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
    const { width, data } = this.props;
    const isZoomed = data[0].x.length > 5;
    const height = isZoomed ?
      100 * data[0].x.length :
      350;

    return (
      <PlotlyChart
        data={data}
        layout={{
          width,
          height: height,
          margin: {t: 0, r: 0, b: 30, l: 20, pad: 0, autoexpand: !isZoomed},
          paper_bgcolor: 'rgba(0, 0, 0, 0)',
          plot_bgcolor: 'rgba(0, 0, 0, 0)',
          legend: {
            xanchor: 'right',
            yanchor: 'top',
            x: .9,
            y: 1.5,
            orientation: 'h',
          },
          barmode: 'group',
          bargap: .5
        }}
        onUpdate={this.fixYLabels.bind(this)}
      />
    );
  }
}

class WinsBarChartWrapper extends React.PureComponent {
  onRequestNewData(path, data) {
    const names = data.map(pluck('procuringEntityName'));
    this.props.requestNewData(path, [{
      x: data.map(pluck('count')),
      y: names,
      name: 'Wins',
      type: 'bar',
      marker: {
        color: '#289df4',
      },
      hoverinfo: 'none',
      orientation: 'h',
    }, {
      x: data.map(pluck('countFlags')),
      y: names,
      name: 'Flags',
      type: 'bar',
      marker: {
        color: '#ce4747',
      },
      hoverinfo: 'none',
      orientation: 'h',
    }]);
  }

  render() {
    const { requestNewData } = this.props;
    if (!requestNewData) return null;

    return (
      <DataFetcher
        {...this.props}
        endpoint="supplierWinsPerProcuringEntity"
        requestNewData={this.onRequestNewData.bind(this)}
      >
        <CustomPopup
          {...this.props}
          Chart={WinsBarChart}
          Popup={Popup}
        />
      </DataFetcher>
    );
  }
}

export default WinsBarChartWrapper;
