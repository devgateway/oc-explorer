/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.ocds;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author mihai
 *
 */
public class Period {

	@Indexed
	
	Date startDate;
	
	@Indexed
	Date endDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
