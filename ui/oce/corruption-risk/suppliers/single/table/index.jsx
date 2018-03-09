import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import translatable from '../../../../translatable';
import { supplierProcurementsData, page, pageSize, supplierProcurementsCount } from './state';

const NAME = 'supplierProcurementsComponent';

class BootstrapTableWrapper extends React.PureComponent {
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

export default class Table extends translatable(React.PureComponent) {
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
    return (
      <div>
        {data.map(datum => {
          const translated = this.t(`crd:corruptionType:${datum.type}:name`);
          return <div>{translated}: {datum.count}</div>;
        })}
      </div>
    );
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

  formatDate(date) {
    return new Date(date).toLocaleDateString();
  }

  formatPE(PEName, { PEId }) {
    return (
      <a href={`#!/crd/procuring-entity/${PEId}`}>
        {PEName}
      </a>
    );
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
            title: this.t('crd:contracts:baseInfo:procuringEntityName'),
            dataField: 'PEName',
            dataFormat: this.formatPE.bind(this),
        }, {
            title: this.t('crd:contracts:list:awardAmount'),
            dataField: 'awardAmount',
        }, {
            title: this.t('crd:contracts:baseInfo:awardDate'),
            dataField: 'awardDate',
            dataFormat: this.formatDate.bind(this),
        }, {
            title: this.t('crd:supplier:table:nrBidders'),
            dataField: 'nrBidders',
            width: '10%',
        }, {
            title: this.t('crd:procurementsTable:noOfFlags'),
            dataField: 'types',
            dataFormat: this.formatTypes.bind(this),
            width: '25%',
        }, {
            title: this.t('crd:supplier:table:flagName'),
            dataField: 'flags',
            dataFormat: this.formatFlags.bind(this),
            width: '25%',
        }]}
      />
    )
  }
}

