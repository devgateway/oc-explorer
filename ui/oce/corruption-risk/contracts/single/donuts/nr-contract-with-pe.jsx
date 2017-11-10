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
    const eps = ['ocds/release/count'];
    const { procuringEntityId } = this.props;
    if (procuringEntityId) {
      eps.push(`ocds/release/count/?procuringEntityId=${procuringEntityId}`)
    }
    return eps;
  }

  transform([total, thisPE]){
    return {
      thisPE,
      total
    }
  }

  componentDidUpdate(prevProps, ...rest) {
    if (this.props.procuringEntityId != prevProps.procuringEntityId) {
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
      values: [data.get('thisPE'), data.get('total')],
      textinfo: 'value',
      textposition: 'none',
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
