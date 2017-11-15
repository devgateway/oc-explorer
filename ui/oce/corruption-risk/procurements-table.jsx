import ReactDOM from 'react-dom';
import Table from '../visualizations/tables';
import translatable from '../translatable';
import { POPUP_HEIGHT } from './constants';

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
    const tenderValue = entry.getIn(['tender', 'value']);
    let awardValue;

    const winningAward = entry.get('awards').find(award => award.get('status') == 'active');
    if (winningAward) {
      awardValue = winningAward.get('value');
    }

    const tenderPeriod = entry.get('tenderPeriod');
    const startDate = new Date(tenderPeriod.get('startDate'));
    const endDate = new Date(tenderPeriod.get('endDate'));
    const flags = entry.get('flags');
    const flaggedStats = flags.get('flaggedStats');
    const type = flaggedStats.get('type');
    const flagIds =
      flags
        .filter(
          flag => flag.has && flag.has('types') && flag.get('types').includes(type) && flag.get('value')
        )
        .keySeq();

    const procuringEntityName = entry.getIn(['procuringEntity', 'name']);
    const title = entry.get('title');
    const id = entry.get('ocid');

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
        <td>{startDate.toLocaleDateString()}&mdash;{endDate.toLocaleDateString()}</td>
        <td>{this.t(`crd:corruptionType:${type}:name`)}</td>
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
    const { data } = this.props;
    return (
      <table className={`table table-striped table-hover ${this.getClassName()}`}>
        <thead>
          <tr>
            <th>{this.t('crd:procurementsTable:status')}</th>
            <th>{this.t('crd:procurementsTable:contractID')}</th>
            <th>{this.t('crd:procurementsTable:title')}</th>
            <th>{this.t('crd:procurementsTable:procuringEntity')}</th>
            <th>{this.t('crd:procurementsTable:tenderAmount')}</th>
            <th>{this.t('crd:procurementsTable:awardsAmount')}</th>
            <th>{this.t('crd:procurementsTable:tenderDate')}</th>
            <th className="flag-type">{this.t('crd:procurementsTable:flagType')}</th>
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
