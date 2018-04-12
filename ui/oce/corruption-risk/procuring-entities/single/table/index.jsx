import translatable from '../../../../translatable';
import BootstrapTableWrapper from '../../../archive/bootstrap-table-wrapper';
import { procurementsData, page, pageSize, procurementsCount } from './state';

const NAME = 'PEProcurementsComponent';

class Table extends translatable(React.PureComponent) {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.data = [];
  }

  componentDidMount() {
    procurementsData.addListener(NAME, () => this.updateBindings());
    page.addListener(NAME, () => this.updateBindings());
    pageSize.addListener(NAME, () => this.updateBindings());
    procurementsCount.addListener(NAME, () => this.updateBindings());
  }

  updateBindings() {
    Promise.all([
      procurementsData.getState(NAME),
      page.getState(NAME),
      pageSize.getState(NAME),
      procurementsCount.getState(NAME),
    ]).then(([data, page, pageSize, procurementsCount]) => {
      this.setState({
        data,
        page,
        pageSize,
        count: procurementsCount,
      })
    })
  }

  componentWillUnmount() {
    procurementsData.removeListener(NAME);
    page.removeListener(NAME);
    pageSize.removeListener(NAME);
    procurementsCount.removeListener(NAME);
  }

  formatFlags(data) {
    return (
      <div>
        {data.map(indicator => (
          <div>
            &#9679; {this.t(`crd:indicators:${indicator}:name`)}
          </div>
        ))}
      </div>
    );
  }

  render() {
    const { data, count } = this.state;

    return (
      <BootstrapTableWrapper
        bordered
        data={data}
        page={this.state.page}
        pageSize={this.state.pageSize}
        onPageChange={newPage => page.assign(NAME, newPage)}
        onSizePerPageList={newPageSize => pageSize.assign(NAME, newPageSize)}
        count={count}
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
