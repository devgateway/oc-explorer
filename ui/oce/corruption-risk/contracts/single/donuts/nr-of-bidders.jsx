import { List } from 'immutable';
import CenterTextDonut from './index.jsx';

const AVG_MOCK = 10;

class NrOfBidders extends CenterTextDonut {
  getCenterText(){
    const { contract } = this.props;
    if (!contract) return '';
    const count = contract.getIn(['tender', 'tenderers'], List()).count();
    return `${count}/${AVG_MOCK}`;
  }

  getTitle(){
    return 'Number of bidders vs average';
  }
}

NrOfBidders.Donut = class extends CenterTextDonut.Donut {
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
        colors: ['#289df5', '#fac329'],
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

NrOfBidders.Donut.endpoint = 'totalFlags';

export default NrOfBidders;
