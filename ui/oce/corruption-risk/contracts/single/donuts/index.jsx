import cn from 'classnames';
import backendYearFilterable from '../../../../backend-year-filterable';
import Chart from '../../../../visualizations/charts/index.jsx';
import style from './style.less';

class CenterTextDonut extends React.PureComponent {
  getClassnames() {
    return ['center-text-donut'];
  }

  getCenterText() {
    return 'Sample text';
  }

  render() {
    const { Donut } = this.constructor;
    return (
      <div className={cn(this.getClassnames())}>
        <Donut
          margin={{ b: 0, t: 0 }}
          height={300}
          {...this.props}
        />
        <h4 className="title">
          {this.getTitle()}
        </h4>
        <div className="center-text">
          {this.getCenterText()}
        </div>
      </div>
    );
  }
}

CenterTextDonut.Donut = class extends backendYearFilterable(Chart){};

export default CenterTextDonut;
