import ReactDOM from 'react-dom';
require('./style.less');
import State, { HVar, Mapping } from '../state';
import OCEState from '../state/oce-state';

class ObjectTable extends React.PureComponent {
  render() {
    const { object } = this.props;
    const sndLvlKeys = new Set();
    const keys = Object.keys(object);
    keys.forEach(key => {
      const subObj = object[key];
      if (typeof subObj === 'undefined') return;
      Object.keys(subObj).forEach(key => sndLvlKeys.add(key));
    });
    return (
      <table className="table table-bordered">
        <thead>
          <tr>
            <th></th>
            {keys.map(key => <th>{key}</th>)}
          </tr>
        </thead>
        <tbody>
          {[...sndLvlKeys].map(sndLvlKey => (
            <tr>
              <th>{sndLvlKey}</th>
              {keys.map(key => <td>{object[key][sndLvlKey]}</td>)}
            </tr>
          ))}
        </tbody>
      </table>
    )
  }
}

class Renderer extends React.PureComponent {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.objectState; 
  }

  componentDidMount() {
    const { object } = this.props;
    object.addListener(
      'dev-tools',
      () => object.getState().then(objectState => {
        this.setState({ objectState })
      })
    )
  }

  getObjectState() {
    const { objectState } = this.state;
    if (typeof objectState === 'undefined') {
      return (
        <strong className="keyword">undefined</strong>
      )
    }

    if (typeof objectState === 'object') {
      const jsified = objectState.toJS ? objectState.toJS() : objectState;
      const fstKey = Object.keys(jsified)[0];
      if (typeof jsified[fstKey] === 'object') {
        return (
          <ObjectTable
            object={objectState.toJS ? objectState.toJS() : objectState}
          />
        )
      }
    }

    return JSON.stringify(objectState);
  }
}

class HVarRenderer extends Renderer {
  render() {
    const { object } = this.props;
    const { name, initial } = object;
    const { objectState } = this.state;
    let formattedInitial;
    return (
      <section className="indent">
        <strong className="keyword">hvar</strong>
        &nbsp;
        {name} =
        {objectState === initial && <strong className="keyword">&nbsp;initial</strong>}
        &nbsp;
        {this.getObjectState()}
      </section>
    )
  }
}

class MappingRenderer extends Renderer {
  render() {
    const { object } = this.props;
    const { name, deps } = object;
    const { objectState } = this.state;
    return (
      <section className="indent">
        <strong className="keyword">mapping</strong>
        &nbsp;
        {name} = {this.getObjectState()} &larr; {deps.join(', ')}
      </section>
    )
  }
}

class StateRenderer extends React.PureComponent {
  render() {
    const { object } = this.props;
    const { entities, name } = object;
    return (
      <section className="indent">
        <header>
          <strong className="keyword">state</strong>
          &nbsp;
          {object.name}
        </header>
        <section>
          {entities.map(entityName => {
             const entity = object[entityName];
             if (entity instanceof HVar) {
               return <HVarRenderer object={entity} />
             }
             if (entity instanceof Mapping) {
               return <MappingRenderer object={entity} />
             }
             if (entity instanceof State) {
               return <StateRenderer object={entity} />
             }
          })}
        </section>
        <footer>
          <strong className="keyword">end state</strong>
          &nbsp;
          {name}
        </footer>
      </section>
    )
  }
}

class DevTools extends React.PureComponent {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.show = false;
  }

  componentDidMount() {
    document.addEventListener('keyup', ({ which }) => {
      if (which === 192) {
        const { show } = this.state;
        this.setState({ show: !show });
      }
    });
  }

  render() {
    const { show } = this.state;
    if (!show) return null;

    return (
      <div id="oce-dev-tools">
        <StateRenderer object={OCEState}/>
      </div>
    );
  }
}

const container = document.createElement('div');
document.body.appendChild(container);

ReactDOM.render(<DevTools/>, container);
