import CenterTextDonut from './index.jsx';

class NrOfContractsWithPE extends CenterTextDonut {
  getClassnames() {
    return super.getClassnames().concat('nr-contracts');
  }

  getCenterText() {
    const { data } = this.props;
    if (!data) return null;
    return (
      <div>
        {data.get('thisPE')}
        <div className="secondary">
          of {data.get('total')}
        </div>
      </div>
    );
  }

  getTitle() {
    return 'Number of contracts with this procuring entity';
  }
}

NrOfContractsWithPE.Donut = class extends CenterTextDonut.Donut {
  getCustomEP() {
    const { procuringEntityId, supplierId } = this.props;
    if (!procuringEntityId || !supplierId) return [];
    return [
      `ocds/release/count/?procuringEntityId=${procuringEntityId}`,
      `ocds/release/count/?procuringEntityId=${procuringEntityId}` +
        `&supplierId=${supplierId}&awardStatus=active`
    ];
  }

  transform([total, thisPE]){
    return {
      thisPE,
      total
    }
  }

  componentDidUpdate(prevProps, ...rest) {
    const peChanged = this.props.procuringEntityId != prevProps.procuringEntityId;
    const supplierChanged = this.props.supplierId != prevProps.supplierId;
    if (peChanged || supplierChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps, ...rest);
    }
  }

  getData() {
    const { contract } = this.props;
    const data = super.getData();
    if (!data) return [];
    return [{
      labels: ['Won by this supplier', 'Contracts with this PE'],
      values: [data.get('thisPE'), data.get('total')],
      hoverlabel: {
        bgcolor: '#144361'
      },
      hoverinfo: 'label',
      textinfo: 'none',
      hole: 0.8,
      type: 'pie',
      marker: {
        colors: ['#72c47e', '#2e833a'],
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

export default NrOfContractsWithPE;
