import backendYearFilterable from '../../../../backend-year-filterable';
import Chart from '../../../../visualizations/charts/index.jsx';

class NrOfBidders extends backendYearFilterable(Chart) {
  getData() {
    const data = super.getData();
    if (!data || !data.count()) return [];
    return [{
      values: [5, 10],
      labels: ['this', 'total'],
      textinfo: 'value',
      hole: 0.85,
      type: 'pie',
      marker: {
        colors: ['#289df5', '#fac329'],
      },
    }];
  }

  getLayout() {
    return {
      title: 'Number of bidders vs average',
      showlegend: false,
      paper_bgcolor: 'rgba(0,0,0,0)',
    };
  }
}

NrOfBidders.endpoint = 'totalFlags';

export default NrOfBidders;
