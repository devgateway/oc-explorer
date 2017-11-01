import CenterTextDonut from './index.jsx';

class PercentPESpending extends CenterTextDonut {
  getCenterText() {
    return '12%';
  }

  getTitle() {
    return '% of procuring entity spending to this supplier';
  }
}

PercentPESpending.Donut = class extends CenterTextDonut.Donut {
  getData() {
    const data = super.getData();
    if (!data || !data.count()) return [];
    return [{
      values: [5, 10],
      labels: ['this', 'total'],
      textinfo: 'value',
      textposition: 'none',
      hole: 0.8,
      type: 'pie',
      marker: {
        colors: ['#40557d', '#289df5'],
      },
    }];
  }

  getLayout() {
    return {
      showlegend: false,
      paper_bgcolor: 'rgba(0,0,0,0)',
    };
  }
}

PercentPESpending.Donut.endpoint = 'totalFlags';

export default PercentPESpending;
