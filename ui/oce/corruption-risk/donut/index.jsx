import cn from 'classnames';
import backendYearFilterable from '../../backend-year-filterable';
import Chart from '../../visualizations/charts';
import { wireProps } from '../tools';
import { pluck } from '../../../tools';
import CustomPopup from '../custom-popup';
import DonutPopup from './popup';

class Donut extends backendYearFilterable(Chart) {
  getCustomEP() {
    return this.props.endpoint || 'ocds/release/count';
  }

  hasNoData() {
    return this.props.data && !this.props.data.length;
  }

  getData() {
    const { data } = this.props;
    if (!data.length) return [];
    return [{
      labels: data.map(pluck('label')),
      values: data.map(pluck('value')),
      hoverlabel: {
        bgcolor: '#144361',
      },
      hoverinfo: 'none',
      textinfo: 'none',
      hole: 0.8,
      type: 'pie',
      marker: {
        colors: data.map(pluck('color')),
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
    const { title, subtitle, className, data, CenterText, styling, translations } = this.props;
    return (
      <div className={cn(className, 'center-text-donut')}>
        <div>
          <Donut
            {...wireProps(this)}
            endpoint={this.props.endpoint}
            margin={{ b: 0, t: 0, r: 0, l: 0, pad: 0 }}
            height={300}
          />
          <CenterText
            data={data}
            styling={styling}
            translations={translations}
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
