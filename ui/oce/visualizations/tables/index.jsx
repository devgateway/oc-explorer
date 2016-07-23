import Visualization from '../../visualization';

class Table extends Visualization{
  buildUrl(ep){
    return super.buildUrl(ep).addSearch('year', this.props.years.toArray());
  }

  componentDidUpdate(prevProps){
    if(this.props.years != prevProps.years){
      this.fetch()
    } else super.componentDidUpdate(prevProps);
  }
}

Table.DATE_FORMAT = {
  year: 'numeric',
  month: 'short',
  day: 'numeric'
};

Table.comparable = false;

export default Table;