import backendYearFilterable from '../../../../backend-year-filterable';
import Chart from '../../../../visualizations/charts/index.jsx';

class NrOfContractsWithPE extends backendYearFilterable(Chart) {
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
        colors: ['#72c47e', '#2e833a'],
      },
    }];
  }

  getLayout() {
    return {
      title: 'Number of contracts with this procuring entity',
      showlegend: false,
      paper_bgcolor: 'rgba(0,0,0,0)',
    };
  }
}

NrOfContractsWithPE.endpoint = 'totalFlags';

export default NrOfContractsWithPE;
