import Range from "./inputs/range";
import {fetchJson} from "../tools";

class TenderPrice extends Range{
    componentDidMount(){
        fetchJson('/api/tenderValueInterval').then(([{minTenderValue: min, maxTenderValue: max}]) => this.setState({min, max}))
        
    }
    getTitle(){
        return this.__('Tender price');
    }
}

export default TenderPrice;
