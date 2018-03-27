import translatable from '../../../../translatable';
import BootstrapTableWrapper from '../../../archive/bootstrap-table-wrapper';
import { procurementsData } from './state';

const NAME = 'PEProcurementsComponent';

class Table extends translatable(React.PureComponent) {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.data = [];
  }

  componentDidMount() {
    procurementsData.addListener(NAME, () => this.updateBindings());
  }

  updateBindings() {
    Promise.all([
      procurementsData.getState(NAME)
    ]).then(([data]) => {
      this.setState({
        data
      })
    })
  }

  componentWillUnmount() {
    procurementsData.removeListener(NAME);
  }

  formatFlags(data) {
    return (
      <div>
        {data.map(indicator => (
          <div>
            {this.t(`crd:indicators:${indicator}:name`)}
          </div>
        ))}
      </div>
    );
  }

  render() {
    const { data } = this.state;

    return (
      <BootstrapTableWrapper
        data={data}
        columns={[{
            title: 'Tender name',
            dataField: 'name',
            width: '20%',
        }, {
            title: 'OCID',
            dataField: 'ocid'
        }, {
            title: 'Award status',
            dataField: 'awardStatus',
        }, {
            title: 'Tender amount',
            dataField: 'tenderAmount',
        }, {
            title: this.t('crd:contracts:list:awardAmount'),
            dataField: 'awardAmount',
        }, {
            title: 'Number of bidders',
            dataField: 'nrBidders',
        }, {
            title: 'Number of flags',
            dataField: 'nrFlags',
        }, {
            title: this.t('crd:supplier:table:flagName'),
            dataField: 'flags',
            dataFormat: this.formatFlags.bind(this),
            width: '20%',
        }]}
      />
    );
  }
}

export default Table;
