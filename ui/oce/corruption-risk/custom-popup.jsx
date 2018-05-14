import ReactDOM from 'react-dom';

class CustomPopup extends React.Component {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state = {
      show: false,
      x: 0,
      y: 0,
    };
  }

  componentDidMount() {
    super.componentDidMount && super.componentDidMount();
    const chartContainer = ReactDOM.findDOMNode(this)
      .querySelector('.js-plotly-plot');

    chartContainer.on('plotly_hover', e => this.showPopup(e));
    chartContainer.on('plotly_unhover', () => this.setState({ show: false }));
    chartContainer.addEventListener('mousemove', this.movePopup.bind(this));
  }

  showPopup({ points }) {
    this.setState({
      show: true,
      points,
    });
  }

  movePopup({ offsetX: x, offsetY: y }) {
    this.setState({ x, y });
  }

  render() {
    const { Popup, Chart } = this.props;
    const { show, x, y, points } = this.state;
    return (
      <div className="custom-popup-container">
        {show &&
          <Popup
            {...this.props}
            y={y}
            x={x}
            points={points}
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
