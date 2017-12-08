import ReactDOM from 'react-dom';

class CustomPopup extends React.Component {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state = {
      show: true,
      x: 0,
      y: 0
    }
  }

  componentDidMount() {
    super.componentDidMount && super.componentDidMount();
    const chartContainer = ReactDOM.findDOMNode(this)
      .querySelector('.js-plotly-plot');

    chartContainer.on('plotly_hover', e => this.setState({ show: true }));
    chartContainer.on('plotly_unhover', e => this.setState({ show: false }));
    chartContainer.addEventListener('mousemove', this.movePopup.bind(this));
  }

  movePopup({ offsetX: x, offsetY: y }) {
    this.setState({ x, y });
  }

  render() {
    const { Popup, Chart } = this.props;
    const { show, x, y } = this.state;
    return (
      <div style={{ position: 'relative' }}>
        {show &&
          <Popup
            {...this.props}
            y={y}
            x={x}
          />
        }
        <Chart
          {...this.props}
        />
      </div>
    );
  }
}

export default CustomPopup;
