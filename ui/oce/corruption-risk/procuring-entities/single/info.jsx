import {
  PEInfo,
  PEFlagsCount,
  associatedBuyers,
  associatedContractsCount,
  associatedUnflaggedContractsCount,
} from './state';
import translatable from '../../../translatable';
import boundComponent from '../../../state/bound-component';

class Cell extends React.PureComponent {
  render() {
    const { title, children, dlClassName, ...props } = this.props;
    return (
      <td {...props}>
        <dl className={dlClassName}>
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
    unflaggedContractsCount: associatedUnflaggedContractsCount,
  }
})) {
  render() {
    const { info, flagsCount, buyers, contractsCount, unflaggedContractsCount } = this.state;
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
                  <span className="count">
                    {flagsCount}
                    &nbsp;
                    {this.t(flagsCount === 1 ?
                      'crd:contracts:baseInfo:flag:sg' :
                      'crd:contracts:baseInfo:flag:pl')}
                  </span>
                  <small>
                    {contractsCount} procurements flagged
                    <br />
                    (Out of {unflaggedContractsCount} procurement won)
                  </small>
                </td>
              </tr>
              {buyers && buyers.length &&
                <tr>
                  <Cell title={this.t('crd:contracts:baseInfo:buyer')} colSpan="3">
                    {buyers.map(buyer => <p>{buyer.buyerName}</p>)}
                  </Cell>
                </tr>
              }
            </tbody>
          </table>
          <table className="table table-bordered info-table">
            <tbody>
              <tr>
                <Cell title="Address" dlClassName="smaller">
                  {address.streetAddress} <br />
                  {address.locality} /
                  &nbsp;
                  {address.postalCode} /
                  &nbsp;
                  {address.countryName}
                </Cell>
                <Cell title="Contacts" colSpan="2" dlClassName="smaller">
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
