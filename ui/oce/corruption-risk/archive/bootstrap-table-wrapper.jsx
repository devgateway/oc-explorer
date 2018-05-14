export default class BootstrapTableWrapper extends React.PureComponent {
  render() {
    const {
      columns,
      data,
      page,
      pageSize,
      onPageChange,
      onSizePerPageList,
      count,
      containerClass,
      bordered
    } = this.props;
    return (
      <BootstrapTable
        data={data}
        striped
        bordered={bordered}
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
        containerClass={containerClass}
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

BootstrapTableWrapper.defaultProps = {
  bordered: false
}
