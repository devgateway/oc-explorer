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
      <div
        onMouseEnter={this.showPopup.bind(this)}
        onMouseLeave={() => this.setState({ showPopup: false })}
      >
        {flaggedStats.get('count')}
        {showPopup && this.getPopup()}
      </div>
    )
  }
}

class ProcurementsTable extends Table{
  renderPopup({ flaggedStats, flagType: type, flagIds }) {
    const { translations } = this.props;
    return (
      <Popup
        flaggedStats={flaggedStats}
        type={type}
        flagIds={flagIds}
        translations={translations}
      />
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

      const flags = contract.get('flags');
      const flaggedStats = flags.get('flaggedStats');
      const flagType = flaggedStats.get('type');
      const flagIds =
        flags
          .filter(
            flag => flag.has && flag.has('types') && flag.get('types').includes(flagType) && flag.get('value')
          )
          .keySeq();

      return {
        status: contract.getIn(['tender', 'status'], 'N/A'),
        id: contract.get('ocid'),
        title: contract.get('title', 'N/A'),
        PEName: contract.getIn(['procuringEntity', 'name'], 'N/A'),
        tenderAmount,
        awardsAmount: getAwardAmount(contract),
        tenderDate: `${startDate}—${endDate}`,
        flagTypeName: this.t(`crd:corruptionType:${flagType}:name`),
        // needed for the popup:
        flaggedStats,
        flagType,
        flagIds
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

        <TableHeaderColumn dataField="flagTypeName">
          {this.t('crd:procurementsTable:flagType')}
        </TableHeaderColumn>

        <TableHeaderColumn
          dataFormat={(_, data) => this.renderPopup(data)}
          columnClassName="hoverable popup-left"
        >
          {this.t('crd:procurementsTable:noOfFlags')}
        </TableHeaderColumn>
      </BootstrapTable>
    );
  }
}



export default ProcurementsTable;
