/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller.request;

/**
 * @author mpostelnicu
 *
 */
public class GroupingFilterPagingRequest extends DefaultFilterPagingRequest {

	String groupByCategory;

	public String getGroupByCategory() {
		return groupByCategory;
	}

	public void setGroupByCategory(String groupByCategory) {
		this.groupByCategory = groupByCategory;
	}
	
}
