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
      labels: values.map(pluck('label')),
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

class DonutWrapper extends React.PureComponent {
  render() {
    const { title, subtitle, className, values, data, CenterText } = this.props;
    return (
      <div className={cn(className, 'center-text-donut')}>
        <div>
          <Donut
            {...wireProps(this)}
            margin={{ b: 0, t: 0, r: 0, l: 0, pad: 0 }}
            height={300}
            data={data}
            values={values}
          />
          <CenterText
            data={data}
            values={values}
          />
        </div>
        <h4 className="title">
          {title}
          {subtitle && [<br />, <small>{subtitle}</small>]}
        </h4>
      </div>
    );
  }
}

class CenterTextDonut extends React.Component {
  render() {
    return (
      <CustomPopup
        {...this.props}
        Popup={DonutPopup}
        Chart={DonutWrapper}
      />
    );
  }
}

export default CenterTextDonut;
