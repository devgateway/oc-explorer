/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo;

import java.util.Date;

/**
 * @author mihai
 *
 */
public class Period {

	Date startDate;
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
