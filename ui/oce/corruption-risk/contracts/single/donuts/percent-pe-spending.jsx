import backendYearFilterable from '../../../../backend-year-filterable';
import Chart from '../../../../visualizations/charts/index.jsx';

class PercentPESpending extends backendYearFilterable(Chart) {
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
        colors: ['#40557d', '#289df5'],
      },
    }];
  }

  getLayout() {
    return {
      title: '% of procuring entity spending to this supplier',
      showlegend: false,
      paper_bgcolor: 'rgba(0,0,0,0)',
    };
  }
}

PercentPESpending.endpoint = 'totalFlags';

export default PercentPESpending;
