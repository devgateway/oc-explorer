import CenterTextDonut from './index.jsx';

class NrOfContractsWithPE extends CenterTextDonut {
  getClassnames() {
    return super.getClassnames().concat('nr-contracts');
  }

  getCenterText() {
    const { contract, data } = this.props;
    if (!contract || !data) return '';
    const peID = contract.getIn(['tender', 'procuringEntity', 'id']);
    const withThisPE = data.filter(c =>
      c.getIn(['tender', 'procuringEntity', 'id']) === peID)
      .count();
    const total = data.count();
    return (
      <div>
        {withThisPE}
        <div className="secondary">
          of {total}
        </div>
      </div>
    );
  }

  getTitle() {
    return 'Number of contracts with this procuring entity';
  }
}

NrOfContractsWithPE.Donut = class extends CenterTextDonut.Donut {
  getData() {
    const { contract } = this.props;
    const data = super.getData();
    if (!data || !data.count() || !contract) return [];
    const peID = contract.getIn(['tender', 'procuringEntity', 'id']);
    const withThisPE = data.filter(c =>
      c.getIn(['tender', 'procuringEntity', 'id']) === peID)
      .count();
    const total = data.count();
    return [{
      values: [withThisPE, total],
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

NrOfContractsWithPE.Donut.endpoint = 'ocds/release/all';

export default NrOfContractsWithPE;
