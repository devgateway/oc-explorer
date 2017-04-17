import FilterBox from "./box";
import {fetchJson, pluck, range} from "../../tools";
import {Set} from "immutable";
import cn from "classnames";

class DateBox extends FilterBox{
	constructor(...args){
		super(...args);
		this.state = {
			years: [],
			months: range(1, 12)
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
		const {years, months} = this.state;
		const selectedYears = state.get('years', Set());
		const selectedMonths = state.get('months', Set());
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
			<p>
				To select a single year and be able to select months,
				hold 'control' while clicking on a year.
			</p>
			<div className="toggleable-wrapper">
				{selectedYears.count() == 1 && months.map(month => {
					 const selected = selectedMonths.has(month) || selectedMonths.count() == 0;
					 const toggleMonth = () => {
						 if(selectedMonths.count() == 0){
							 onUpdate('months', Set(months).delete(month));
						 } else if(selectedMonths.has(month)){
							 onUpdate('months', selectedMonths.delete(month));
						 } else {
							 onUpdate('months', selectedMonths.add(month));
						 }
					 }
					 return (
						 <span
								 key={month}
								 className={cn('toggleable-item', {selected})}
								 onClick={toggleMonth}
						 >
							 {this.t(`general:months:${month}`)}
						 </span>
					 )
				 })}
			</div>
			</div>
		)
	}
}

export default DateBox;
