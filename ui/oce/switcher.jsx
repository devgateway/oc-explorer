class OCESwitcher extends React.Component{
  constructor(...args){
    super(...args);
    this.state={
      view: Object.keys(this.constructor.views)[0]
    }
  }

  render(){
    const {translations} = this.props;
    const CurrentView = this.constructor.views[this.state.view];
    return <CurrentView
               onSwitch={view => this.setState({view})}
               translations={translations}
           />;
  }
}

OCESwitcher.views = {};

export default OCESwitcher;
