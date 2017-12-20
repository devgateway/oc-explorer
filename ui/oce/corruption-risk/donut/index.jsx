import cn from 'classnames';
import backendYearFilterable from '../../backend-year-filterable';
import Chart from '../../visualizations/charts';
import { wireProps } from '../tools';
import { pluck } from '../../../tools';
import CustomPopup from '../custom-popup';
import DonutPopup from './popup';

class Donut extends backendYearFilterable(Chart) {
  getCustomEP() {
    return 'ocds/release/count';
  }

  getData() {
    const { data, values } = this.props;
    return [{
      labels: ['a', 'b'],
      values: data,
      hoverlabel: {
        bgcolor: '#144361',
      },
      hoverinfo: 'none',
      textinfo: 'none',
      hole: 0.8,
      type: 'pie',
      marker: {
        colors: values.map(pluck('color')),
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

class CenterTextDonut extends React.PureComponent {
  render() {
    const { title, subtitle, className, values, data, CenterText } = this.props;
    return (
      <div className={cn(className, 'center-text-donut')}>
        <div>
          <CustomPopup
            {...wireProps(this)}
            Popup={DonutPopup}
            Chart={Donut}
            margin={{ b: 0, t: 0, r: 0, l: 0, pad: 0 }}
            height={300}
            data={data}
            values={values}
          />
          <CenterText
            data={data}
          />
          <div className="center-text">
          </div>
        </div>
        <h4 className="title">
          {title}
          {subtitle && [<br />, <small>{subtitle}</small>]}
        </h4>
      </div>
    );
  }
}

export default CenterTextDonut;
