import CenterTextDonut from './index.jsx';

class PercentPESpending extends CenterTextDonut {
  getCenterText() {
    const { data } = this.props;
    if (!data) return null;
    return (
      <span>
        &nbsp;
        {data.get('percentage').toFixed(2)}
        %
      </span>
    );
  }

  getTitle() {
    return '% of procuring entity spending to this supplier';
  }
}

PercentPESpending.Donut = class extends CenterTextDonut.Donut {
  getCustomEP() {
    const { procuringEntityId, supplierId } = this.props;
    return `percentageAmountAwarded?procuringEntityId=${procuringEntityId}&supplierId=${supplierId}`;
  }

  transform(data){
    const { percentage, totalAwarded, totalAwardedToSuppliers } = data[0];
    return {
      percentage,
      total: totalAwarded.sum,
      toSuppliers: totalAwardedToSuppliers.sum
    }
  }

  getData() {
    const data = super.getData();
    if (!data || !data.count()) return [];
    return [{
      values: [data.get('toSuppliers'), data.get('total')],
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

export default PercentPESpending;
