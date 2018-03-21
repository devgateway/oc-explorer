export default function boundComponent({ name, deps }) {
  return class extends React.PureComponent {
    constructor(...args){
      super(...args);
      this.state = this.state || {};
    }

    componentWillMount() {
      Object.values(deps).forEach(dep =>
        dep.addListener(name, this.updateBindings.bind(this))
      );
    }

    componentWillUnmount() {
      Object.values(deps).forEach(dep =>
        dep.removeListener(name)
      );
    }

    updateBindings() {
      Promise.all(
        Object.values(deps).map(dep => dep.getState(name))
      ).then(data => {
        const newState = {};
        Object.keys(deps).forEach(
          (depName, index) => newState[depName] = data[index]
        );
        this.setState(newState);
      });
    }
  }
}
