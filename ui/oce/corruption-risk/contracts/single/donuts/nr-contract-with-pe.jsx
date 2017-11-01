import CenterTextDonut from './index.jsx';

class NrOfContractsWithPE extends CenterTextDonut {
  getCenterText(){
    const { contract } = this.props;
    if (!contract) return '';
    return (
      <div>
        10
        <small>
          of 14
        </small>
      </div>)
  }

  getTitle(){
    return 'Number of contracts with this procuring entity';
  }
}

NrOfContractsWithPE.Donut = class extends CenterTextDonut.Donut {
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
}

NrOfContractsWithPE.Donut.endpoint = 'totalFlags';

export default NrOfContractsWithPE;
