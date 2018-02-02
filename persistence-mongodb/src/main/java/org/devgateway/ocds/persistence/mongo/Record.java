package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Record
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ocid",
        "releases",
        "compiledRelease",
        "versionedRelease"
})
public class Record {

    /**
     * Open Contracting ID
     * <p>
     * A unique identifier that identifies the unique Open Contracting Process. For more information see:
     * http://standard.open-contracting.org/latest/en/getting_started/contracting_process/
     * (Required)
     */
    @JsonProperty("ocid")
    @JsonPropertyDescription("A unique identifier that identifies the unique Open Contracting Process. For more "
            + "information see: http://standard.open-contracting.org/latest/en/getting_started/contracting_process/")
    private String ocid;
    /**
     * Releases
     * <p>
     * An array of linking identifiers or releases
     * (Required)
     */
    @JsonProperty("releases")
    @JsonPropertyDescription("An array of linking identifiers or releases")
    private List<Release> releases = new ArrayList<Release>();
    /**
     * Schema for an Open Contracting Release
     * <p>
     */
    @JsonProperty("compiledRelease")
    private Release compiledRelease;
    /**
     * Schema for a compiled, versioned Open Contracting Release.
     * <p>
     */
    @JsonProperty("versionedRelease")
    private Release versionedRelease;

    /**
     * Open Contracting ID
     * <p>
     * A unique identifier that identifies the unique Open Contracting Process. For more information see:
     * http://standard.open-contracting.org/latest/en/getting_started/contracting_process/
     * (Required)
     */
    @JsonProperty("ocid")
    public String getOcid() {
        return ocid;
    }

    /**
     * Open Contracting ID
     * <p>
     * A unique identifier that identifies the unique Open Contracting Process. For more information see:
     * http://standard.open-contracting.org/latest/en/getting_started/contracting_process/
     * (Required)
     */
    @JsonProperty("ocid")
    public void setOcid(String ocid) {
        this.ocid = ocid;
    }

    /**
     * Releases
     * <p>
     * An array of linking identifiers or releases
     * (Required)
     */
    @JsonProperty("releases")
    public List<Release> getReleases() {
        return releases;
    }

    /**
     * Releases
     * <p>
     * An array of linking identifiers or releases
     * (Required)
     * @param releases
     */
    @JsonProperty("releases")
    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

    /**
     * Schema for an Open Contracting Release
     * <p>
     */
    @JsonProperty("compiledRelease")
    public Release getCompiledRelease() {
        return compiledRelease;
    }

    /**
     * Schema for an Open Contracting Release
     * <p>
     */
    @JsonProperty("compiledRelease")
    public void setCompiledRelease(Release compiledRelease) {
        this.compiledRelease = compiledRelease;
    }

    /**
     * Schema for a compiled, versioned Open Contracting Release.
     * <p>
     */
    @JsonProperty("versionedRelease")
    public Release getVersionedRelease() {
        return versionedRelease;
    }

    /**
     * Schema for a compiled, versioned Open Contracting Release.
     * <p>
     */
    @JsonProperty("versionedRelease")
    public void setVersionedRelease(Release versionedRelease) {
        this.versionedRelease = versionedRelease;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ocid", ocid)
                .append("releases", releases)
                .append("compiledRelease", compiledRelease)
                .append("versionedRelease", versionedRelease)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(compiledRelease)
                .append(versionedRelease)
                .append(ocid)
                .append(releases)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Record)) {
            return false;
        }
        Record rhs = ((Record) other);
        return new EqualsBuilder().append(compiledRelease, rhs.compiledRelease)
                .append(versionedRelease, rhs.versionedRelease)
                .append(ocid, rhs.ocid)
                .append(releases, rhs.releases)
                .isEquals();
    }

}
