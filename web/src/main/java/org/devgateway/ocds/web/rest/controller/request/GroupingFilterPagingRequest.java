/**
 * 
 */
package org.devgateway.ocds.web.rest.controller.request;

/**
 * @author mpostelnicu
 *
 */
public class GroupingFilterPagingRequest extends DefaultFilterPagingRequest {

	private String groupByCategory;

	public String getGroupByCategory() {
		return groupByCategory;
	}

	public void setGroupByCategory(final String groupByCategory) {
		this.groupByCategory = groupByCategory;
	}

}
