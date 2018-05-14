import cn from 'classnames';
import backendYearFilterable from '../../../../backend-year-filterable';
import Chart from '../../../../visualizations/charts/index.jsx';
import style from './style.less';
import translatable from '../../../../translatable';

class CenterTextDonut extends translatable(React.PureComponent) {
  getClassnames() {
    return ['center-text-donut'];
  }

  render() {
    const { Donut } = this.constructor;
    return (
      <div className={cn(this.getClassnames())}>
        <div>
          <Donut
            margin={{ b: 0, t: 0, r: 0, l: 0, pad: 0 }}
            height={300}
            {...this.props}
          />
          <div className="center-text">
            {this.getCenterText()}
          </div>
        </div>
        <h4 className="title">
          {this.getTitle()}
        </h4>
      </div>
    );
  }
}

CenterTextDonut.Donut = class extends backendYearFilterable(Chart){};

export default CenterTextDonut;
