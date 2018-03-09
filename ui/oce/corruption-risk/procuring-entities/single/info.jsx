import { PEInfo, PEFlagsCount, associatedBuyers, associatedContractsCount } from './state';
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
    const { title, children, ...props } = this.props;
    return (
      <td {...props}>
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
    buyers: associatedBuyers,
    contractsCount: associatedContractsCount,
  }
})) {
  render() {
    const { info, flagsCount, buyers, contractsCount } = this.state;
    if (!info) return null;
    const { address, contactPoint } = info;

    return (
      <div className="pe-page">
        <section className="info">
          <table className="table join-bottom table-bordered info-table">
            <tbody>
              <tr>
                <Cell title={this.t('crd:contracts:baseInfo:procuringEntityName')}>
                  {info.name}
                </Cell>
                <Cell title={this.t('crd:suppliers:ID')}>{info.id}</Cell>
                <td className="flags">
                  <img src="assets/icons/flag.svg" alt="Flag icon" className="flag-icon" />
                  &nbsp;
                  {flagsCount}
                  &nbsp;
                  {this.t(flagsCount === 1 ?
                    'crd:contracts:baseInfo:flag:sg' :
                    'crd:contracts:baseInfo:flag:pl')}

                  <br />
                  <small>
                    (
                      {contractsCount}
                      &nbsp;
                      {this.t(contractsCount === 1 ?
                        'crd:supplier:contract:sg' :
                        'crd:supplier:contract:pl')}
                    )
                  </small>
                </td>
              </tr>
              {buyers && buyers.length &&
                <tr>
                  <Cell title={this.t('crd:contracts:baseInfo:buyer')} colSpan="3">
                    {buyers.map(buyer => <p>{buyer}</p>)}
                  </Cell>
                </tr>
              }
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
                <Cell title="Contacts" colSpan="2">
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
