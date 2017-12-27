import { List } from 'immutable';
import CenterTextDonut from './index.jsx';

class NrOfBidders extends CenterTextDonut {
  getClassnames() {
    return super.getClassnames().concat('nr-of-bidders');
  }

  getCenterText() {
    const { count, data: avg } = this.props;
    if (isNaN(avg) || isNaN(count)) return '';
    return (
      <div>
        {count}
        <span className="secondary">/{avg.toFixed(2)}</span>
      </div>
    );
  }

  getTitle() {
    return 'Number of bidders vs average';
  }
}

NrOfBidders.Donut = class extends CenterTextDonut.Donut {
  transform(data) {
    try {
      return data[0].averageNoTenderers;
    } catch(_) {
      return 0;
    }
  }

  getData() {
    const avg = super.getData();
    const { count } = this.props;
    if (isNaN(avg) || isNaN(count)) return [];
    return [{
      labels: [
        this.t('crd:contract:nrBiddersVsAvg:thisLabel'),
        this.t('crd:contract:nrBiddersVsAvg:avgLabel')
      ],
      values: [count, avg],
      hoverlabel: {
        bgcolor: '#144361'
      },
      hoverinfo: 'none',
      textinfo: 'none',
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

NrOfBidders.Donut.endpoint = 'averageNumberOfTenderers';
NrOfBidders.Donut.UPDATABLE_FIELDS = CenterTextDonut.Donut.UPDATABLE_FIELDS.concat('count');

export default NrOfBidders;
