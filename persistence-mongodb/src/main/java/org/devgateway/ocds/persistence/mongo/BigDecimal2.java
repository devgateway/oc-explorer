/**
 * 
 */
package org.devgateway.ocds.persistence.mongo;

import java.math.BigDecimal;

/**
 * @author mihai This is to overcome spring-data-mongodb cyclic verification has
 *         false positives. In our case, we need Value2 with BigDecimal2 to
 *         ensure its path is distinct from Value/BigDecimal (hope this will be
 *         fixed in future versions of spring data mongodb)
 * @see https://jira.spring.io/browse/DATAMONGO-1283?jql=project%20%3D%
 *      20DATAMONGO%20AND%20text%20~%20%22CyclicPropertyReferenceException%22%
 *      20ORDER%20BY%20created%20DESC
 */
public class BigDecimal2 extends BigDecimal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BigDecimal2(String val) {
		super(val);
	}

	public BigDecimal2(Double val) {
		super(val);
	}

}
