package org.devgateway.toolkit.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * @author idobre
 * @since 11/13/14
 *
 * Entity used to store the content of uploaded files
 */

@Entity
public class FileContent extends AbstractAuditableEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Lob
    @Column(length = 10000000)
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

	/* (non-Javadoc)
	 * @see org.devgateway.ccrs.persistence.dao.AbstractAuditableEntity#getParent()
	 */
	@Override
	public AbstractAuditableEntity getParent() {
		return null;
	}
}
