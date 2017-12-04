import ReactDOM from 'react-dom';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import Table from '../visualizations/tables';
import translatable from '../translatable';
import { POPUP_HEIGHT } from './constants';
import { getAwardAmount, mkContractLink } from './tools';

// eslint-disable-next-line no-undef
class Popup extends translatable(React.Component){
  constructor(...args) {
    super(...args);
    this.state = {
      showPopup: false,
    };
  }

  getPopup(){
    const {type, flagIds} = this.props;
    const {popupTop} = this.state;
    return (
      <div className="crd-popup text-center" style={{top: popupTop}}>
        <div className="row">
          <div className="col-sm-12 info">
            <h5>{this.t('crd:procurementsTable:associatedFlags').replace('$#$', this.t(`crd:corruptionType:${type}:name`))}</h5>
          </div>
          <div className="col-sm-12">
            <hr />
          </div>
          <div className="col-sm-12 info">
            {flagIds.map(flagId => <p key={flagId}>{this.t(`crd:indicators:${flagId}:name`)}</p>)}
          </div>
        </div>
        <div className="arrow" />
      </div>
    );
  }

  showPopup() {
    const el = ReactDOM.findDOMNode(this);
    this.setState({
      showPopup: true,
      popupTop: -(POPUP_HEIGHT / 2) + (el.offsetHeight / 4)
    });
  }

  render(){
    const {flaggedStats} = this.props;
    const {showPopup} = this.state;
    return (
      <td
        className="hoverable popup-left"
        onMouseEnter={this.showPopup.bind(this)}
        onMouseLeave={() => this.setState({ showPopup: false })}
      >
        {flaggedStats.get('count')}
        {showPopup && this.getPopup()}
      </td>
    )
  }
}

class ProcurementsTable extends Table{
  row(entry, index){
    const { translations, navigate } = this.props;

    const flags = entry.get('flags');
    const flaggedStats = flags.get('flaggedStats');
    const type = flaggedStats.get('type');
    const flagIds =
      flags
        .filter(
          flag => flag.has && flag.has('types') && flag.get('types').includes(type) && flag.get('value')
        )
        .keySeq();

    return (
      <tr key={index}>
        <td>{entry.get('tag', []).join(', ')}</td>
        <td>
          <a
            href="javascript:void(0);"
            onClick={() => navigate('contract', id)}
          >
            {id}
          </a>
        </td>
        <td>
          <div className="oce-3-line-text" title={title}>
            <a
              href="javascript:void(0);"
              onClick={() => navigate('contract', id)}
            >
              {title}
            </a>
          </div>
        </td>
        <td>
          <div title={procuringEntityName} className="oce-3-line-text">
            {procuringEntityName}
          </div>
        </td>
        <td>{tenderValue && tenderValue.get('amount')} {tenderValue && tenderValue.get('currency')}</td>
        <td>
          {awardValue ?
            awardValue.get('amount') + ' ' + awardValue.get('currency') :
            'N/A'
          }
        </td>
        <Popup
          flaggedStats={flaggedStats}
          type={type}
          flagIds={flagIds}
          translations={translations}
        />
      </tr>
    );
  }

  render() {
    const { data, navigate } = this.props;

    if (!data) return null;

    const jsData = data.map((contract) => {
      const tenderAmount = contract.getIn(['tender', 'value', 'amount'], 'N/A') +
          ' ' +
          contract.getIn(['tender', 'value', 'currency'], '');

      const tenderPeriod = contract.get('tenderPeriod');
      const startDate = new Date(tenderPeriod.get('startDate')).toLocaleDateString();
      const endDate = new Date(tenderPeriod.get('endDate')).toLocaleDateString();

      const flagType = contract.getIn(['flags', 'flaggedStats', 'type']);

      return {
        status: contract.getIn(['tender', 'status'], 'N/A'),
        id: contract.get('ocid'),
        title: contract.get('title', 'N/A'),
        PEName: contract.getIn(['procuringEntity', 'name'], 'N/A'),
        tenderAmount,
        awardsAmount: getAwardAmount(contract),
        tenderDate: `${startDate}—${endDate}`,
        flagType: this.t(`crd:corruptionType:${flagType}:name`)
      }
    })

    return (
      <BootstrapTable
        data={jsData}
        striped
        bordered={false}
      >
        <TableHeaderColumn dataField="status">
          {this.t('crd:procurementsTable:status')}
        </TableHeaderColumn>

        <TableHeaderColumn isKey dataField="id" dataFormat={mkContractLink(navigate)}>
          {this.t('crd:procurementsTable:contractID')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="title" dataFormat={mkContractLink(navigate)}>
          {this.t('crd:procurementsTable:title')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="PEName">
          {this.t('crd:procurementsTable:procuringEntity')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="tenderAmount">
          {this.t('crd:procurementsTable:tenderAmount')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="awardsAmount">
          {this.t('crd:procurementsTable:awardsAmount')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="tenderDate">
          {this.t('crd:procurementsTable:tenderDate')}
        </TableHeaderColumn>

        <TableHeaderColumn dataField="flagType">
          {this.t('crd:procurementsTable:flagType')}
        </TableHeaderColumn>

        <TableHeaderColumn>

        </TableHeaderColumn>
      </BootstrapTable>
    );

    return (
      <table className={`table table-striped table-hover ${this.getClassName()}`}>
        <thead>
        <tr>
          <th>{this.t('crd:procurementsTable:noOfFlags')}</th>
        </tr>
        </thead>
        <tbody>
        {data && data.map(this.row.bind(this))}
        </tbody>
      </table>
    );
  }
}



export default ProcurementsTable;
