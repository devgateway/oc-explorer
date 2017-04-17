import FilterBox from "./box";
import {fetchJson, pluck} from "../../tools";
import {Set} from "immutable";
import cn from "classnames";

const range = (from, to) => from > to ?
													[] :
													[from].concat(range(from+1, to));

class DateBox extends FilterBox{
	constructor(...args){
		super(...args);
		this.state = {
			years: []
		}
	}

	componentDidMount(){
		fetchJson('/api/tendersAwardsYears').then(data => {
			const years = data.map(pluck('_id'));
			this.setState({years});
    })
	}

	getTitle(){
		return 'Date';
	}

	getBox(){
		const {onUpdate, translations, state} = this.props;
		const {years} = this.state;
		const selectedYears = state.get('years', Set());
		return (
			<div className="box-content box-date">
			{years.map(year => {
				const selected = selectedYears.has(year) || selectedYears.count() == 0;

				const toggleOtherYears = () => {
					if(selectedYears.count() == 1 && selectedYears.has(year)){
						onUpdate('years', Set(years));
					} else {
						onUpdate('years', Set([year]));
					}
				}

				const toggleYear = e => {
					if(e.ctrlKey){
						toggleOtherYears()
					}else if(selectedYears.count() == 0){
						onUpdate('years', Set(years).delete(year));
					} else if(selectedYears.has(year)){
						onUpdate('years', selectedYears.delete(year));
					} else {
						onUpdate('years', selectedYears.add(year));
					}
				}

				return (
					<span
							key={year}
							className={cn('toggleable-item', {selected})}
							onClick={toggleYear}
					>
						{year}
					</span>
				)
			})}
			<p>To select a single year and be able to select months, hold 'control' while clicking on a year.</p>
			</div>
		)
	}
}

export default DateBox;
