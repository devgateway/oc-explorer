import CenterTextDonut from '../index.jsx';

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

  transform(data) {
    try {
      const { percentage, totalAwarded, totalAwardedToSuppliers } = data[0];
      return {
        percentage,
        total: totalAwarded.sum,
        toSuppliers: totalAwardedToSuppliers.sum,
      };
    } catch (e) {
      return {
        percentage: 0,
        total: 0,
        toSuppliers: 0,
      };
    }
  }

  componentDidUpdate(prevProps, ...rest) {
    const peChanged = this.props.procuringEntityId !== prevProps.procuringEntityId;
    const supplierChanged = this.props.supplierId !== prevProps.supplierId;
    if (peChanged || supplierChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps, ...rest);
    }
  }

  getData() {
    const data = super.getData();
    if (!data || !data.count()) return [];
    const toSuppliers = data.get('toSuppliers');
    const total = data.get('total');
    return [{
      labels: [
        this.t('crd:contract:percentPEspending:this'),
        this.t('crd:contract:percentPEspending:match'),
      ],
      values: [toSuppliers, total-toSuppliers],
      hoverlabel: {
        bgcolor: '#144361',
      },
      hoverinfo: 'none',
      textinfo: 'none',
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
};

export default PercentPESpending;
