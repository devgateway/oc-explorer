import ProcurementMethod from "../../oce/visualizations/charts/procurement-method";

class BidSelectionMethod extends ProcurementMethod{}

BidSelectionMethod.endpoint = 'tenderPriceByVnTypeYear';
BidSelectionMethod.getName = __ => __('Bid selection method');

export default BidSelectionMethod;