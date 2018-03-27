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

  render() {
    const { data } = this.state;
    /*
    Number of flags
    Flag name
    */
    return (
      <BootstrapTableWrapper
        data={data}
        columns={[{
            title: 'Tender name',
            dataField: 'name'
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
        }]}
      />
    );
  }
}

export default Table;
