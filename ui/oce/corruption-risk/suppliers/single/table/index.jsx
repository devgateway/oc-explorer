import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import translatable from '../../../../translatable';
import { supplierProcurementsData } from './state';

class BootstrapTableWrapper extends translatable(React.PureComponent) {
  render() {
    const { columns, data } = this.props;
    return (
      <BootstrapTable
        data={data}
      >
        <TableHeaderColumn dataField="id" isKey hidden>
          hui
        </TableHeaderColumn>
        {columns.map(({ title, ...props }) => (
          <TableHeaderColumn {...props}>
            {title}
          </TableHeaderColumn>
        ))}
      </BootstrapTable>
    )
  }
}

const NAME = 'supplierProcurementsComponent';

export default class Table extends React.PureComponent {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.data = [];
  }

  componentDidMount() {
    supplierProcurementsData.addListener(
      NAME,
      () => {
        supplierProcurementsData.getState(NAME).then(data =>
          this.setState({
            data,
          })
        )
      }
    )
  }

  componentWillUnmount() {
    supplierProcurementsData.removeListener(NAME);
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
    const { data } = this.state;
    return (
      <BootstrapTableWrapper
        data={data}
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

