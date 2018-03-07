import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import translatable from '../../../../translatable';
import { supplierProcurementsData, page, pageSize, supplierProcurementsCount } from './state';

const NAME = 'supplierProcurementsComponent';

class BootstrapTableWrapper extends translatable(React.PureComponent) {
  render() {
    const { columns, data, page, pageSize, onPageChange, onSizePerPageList, count } = this.props;
    return (
      <BootstrapTable
        data={data}
        striped
        bordered={false}
        pagination
        remote
        fetchInfo={{
          dataTotalSize: count,
        }}
        options={{
          page,
          onPageChange,
          sizePerPage: pageSize,
          sizePerPageList: [20, 50, 100, 200].map(value => ({ text: value, value })),
          onSizePerPageList,
          paginationPosition: 'both',
        }}
      >
        <TableHeaderColumn dataField="id" isKey hidden/>
        {columns.map(({ title, ...props }) => (
          <TableHeaderColumn {...props}>
            {title}
          </TableHeaderColumn>
        ))}
      </BootstrapTable>
    )
  }
}

export default class Table extends React.PureComponent {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.data = [];
  }

  componentDidMount() {
    supplierProcurementsData.addListener(NAME, () => this.updateBindings());
    page.addListener(NAME, () => this.updateBindings());
    pageSize.addListener(NAME, () => this.updateBindings());
    supplierProcurementsCount.addListener(NAME, () => this.updateBindings());
  }

  componentWillUnmount() {
    supplierProcurementsData.removeListener(NAME);
    page.removeListener(NAME);
    pageSize.removeListener(NAME);
    supplierProcurementsCount.removeListener(NAME);
  }

  updateBindings() {
    Promise.all([
      supplierProcurementsData.getState(NAME),
      page.getState(NAME),
      pageSize.getState(NAME),
      supplierProcurementsCount.getState(NAME),
    ]).then(([data, page, pageSize, count]) => {
      this.setState({
        data,
        page,
        pageSize,
        count,
      })
    })
  }

  formatTypes(data) {
    return data
      .map(datum => `${datum.type}: ${datum.count}`)
      .join('; ');
  }

  formatFlags(data) {
    return data.join(', ');
  }

  render() {
    const { data, count } = this.state;
    return (
      <BootstrapTableWrapper
        data={data}
        page={this.state.page}
        pageSize={this.state.pageSize}
        onPageChange={newPage => page.assign(NAME, newPage)}
        onSizePerPageList={newPageSize => pageSize.assign(NAME, newPageSize)}
        count={count}
        columns={[{
            title: 'PE Name',
            dataField: 'PEName',
        }, {
            title: 'Award amount',
            dataField: 'awardAmount',
        }, {
            title: 'Award date',
            dataField: 'awardDate',
        }, {
            title: 'No of bidders',
            dataField: 'nrBidders',
        }, {
            title: 'No of flags',
            dataField: 'types',
            dataFormat: this.formatTypes.bind(this),
        }, {
            title: 'Flag Name',
            dataField: 'flags',
            dataFormat: this.formatFlags.bind(this),
        }]}
      />
    )
  }
}

