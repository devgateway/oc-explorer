/**
 * 
 */
package org.devgateway.ocvn.persistence.mongo;

import java.util.Date;
import java.util.List;

/**
 * @author mihai
 *
 */
public class Amendment {
	Date date;
	List<Change> changes;
	String rationale;

	public class Change {
		String property;
		String former_value;

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public String getFormer_value() {
			return former_value;
		}

		public void setFormer_value(String former_value) {
			this.former_value = former_value;
		}

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Change> getChanges() {
		return changes;
	}

	public void setChanges(List<Change> changes) {
		this.changes = changes;
	}

	public String getRationale() {
		return rationale;
	}

	public void setRationale(String rationale) {
		this.rationale = rationale;
	}
}
