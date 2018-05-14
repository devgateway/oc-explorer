import CatChart from "./cat-chart";

class ProcurementMethod extends CatChart {
  static getName(t){return t('charts:procurementMethod:title')}

  static getCatName(datum, t){
    return datum.get(this.CAT_NAME_FIELD) || t('charts:procurementMethod:unspecified');
  }

  getLayout(){
    return {
      xaxis: {
        title: this.t('charts:procurementMethod:xAxisName'),
        type: "category"
      },
      yaxis: {
        title: this.t('charts:procurementMethod:yAxisName'),
        tickprefix: "   "
      }
    }
  }

  getDecoratedLayout() {
    if(window.innerWidth > 1600) return super.getDecoratedLayout();
    const layout = JSON.parse(JSON.stringify(super.getDecoratedLayout()));
    layout.margin.b = 150;
    layout.margin.r = 100;
    return layout;
  }
}

ProcurementMethod.endpoint = 'tenderPriceByProcurementMethod';
ProcurementMethod.excelEP = 'procurementMethodExcelChart';
ProcurementMethod.CAT_NAME_FIELD = 'procurementMethod';
ProcurementMethod.CAT_VALUE_FIELD = 'totalTenderAmount';

export default ProcurementMethod;
