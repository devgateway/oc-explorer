class CustomPopup extends React.Component {
  render() {
    const { Chart } = this.props;
    return (
      <Chart
        {...this.props}
      />
    );
  }
}

export default CustomPopup;
