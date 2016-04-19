/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo.ocds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mihai Amendment OCDS entity
 *         http://standard.open-contracting.org/latest/en/schema/reference/#
 *         amendment
 */
@Document
public class Amendment {

	Date date;
	List<Change> changes = new ArrayList<>();
	String rationale;

	public class Change {
		String property;
		String former_value;

		public String getProperty() {
			return property;
		}

		public void setProperty(final String property) {
			this.property = property;
		}

		public String getFormer_value() {
			return former_value;
		}

		public void setFormer_value(final String former_value) {
			this.former_value = former_value;
		}

	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public List<Change> getChanges() {
		return changes;
	}

	public void setChanges(final List<Change> changes) {
		this.changes = changes;
	}

	public String getRationale() {
		return rationale;
	}

	public void setRationale(final String rationale) {
		this.rationale = rationale;
	}
}
