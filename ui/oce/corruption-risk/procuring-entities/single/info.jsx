import { PEInfo, PEFlagsCount } from './state';
import translatable from '../../../translatable';

function boundComponent({ name, deps }) {
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

class Cell extends React.PureComponent {
  render() {
    const { title, children } = this.props;
    return (
      <td>
        <dl>
          <dt>{title}</dt>
          <dd>{children}</dd>
        </dl>
      </td>
    );
  }
}

class Info extends translatable(boundComponent({
  name: 'PE info',
  deps: {
    info: PEInfo,
    flagsCount: PEFlagsCount,
  }
})) {
  render() {
    const { info, flagsCount } = this.state;
    if (!info) return null;
    const { address, contactPoint } = info;

    return (
      <div className="pe-page">
        <section className="info">
          <table className="table table-bordered join-bottom info-table">
            <tbody>
              <tr>
                <Cell title="Name">{info.name}</Cell>
                <Cell title="ID">{info.id}</Cell>
                <td className="flags">
                  <img src="assets/icons/flag.svg" alt="Flag icon" className="flag-icon" />
                  &nbsp;
                  {flagsCount}
                  &nbsp;
                  {this.t(flagsCount === 1 ?
                    'crd:contracts:baseInfo:flag:sg' :
                    'crd:contracts:baseInfo:flag:pl')}
                </td>
              </tr>
              <tr>
                <td colSpan="3">
                  buyers
                </td>
              </tr>
            </tbody>
          </table>
          <table className="table table-bordered info-table">
            <tbody>
              <tr>
                <Cell title="Address">
                  {address.streetAddress} <br />
                  {address.locality} /
                  &nbsp;
                  {address.postalCode} /
                  &nbsp;
                  {address.countryName}
                </Cell>
                <Cell title="Contacts">
                  {contactPoint.name}<br />
                  {contactPoint.email}<br />
                  {contactPoint.telephone}
                </Cell>
              </tr>
            </tbody>
          </table>
        </section>
      </div>
    );
  }
}

export default Info;
