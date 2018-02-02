package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.net.URI;
import java.util.Date;


/**
 * Document
 * <p>
 * Links to, or descriptions of, external documents can be attached at various locations within the standard.
 * Documents may be supporting information, formal notices, downloadable forms, or any other kind of resource that
 * should be made public as part of full open contracting.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "documentType",
        "title",
        "description",
        "url",
        "datePublished",
        "dateModified",
        "format",
        "language"
})
public class Document {

    /**
     * ID
     * <p>
     * A local, unique identifier for this document. This field is used to keep track of multiple revisions of a
     * document through the compilation from release to record mechanism.
     * (Required)
     */
    @JsonProperty("id")
    @JsonPropertyDescription("A local, unique identifier for this document. This field is used to keep track of "
            + "multiple revisions of a document through the compilation from release to record mechanism.")
    private String id;
    /**
     * Document type
     * <p>
     * A classification of the document described taken from the [documentType codelist](http://standard
     * .open-contracting.org/latest/en/schema/codelists/#document-type). Values from the provided codelist should be
     * used wherever possible, though extended values can be provided if the codelist does not have a relevant code.
     */
    @JsonProperty("documentType")
    @JsonPropertyDescription("A classification of the document described taken from the [documentType codelist]"
            + "(http://standard.open-contracting.org/latest/en/schema/codelists/#document-type). Values from the "
            + "provided codelist should be used wherever possible, though extended values can be provided if the "
            + "codelist does not have a relevant code.")
    private String documentType;
    /**
     * Title
     * <p>
     * The document title.
     */
    @JsonProperty("title")
    @JsonPropertyDescription("The document title.")
    private String title;
    /**
     * Description
     * <p>
     * A short description of the document. We recommend descriptions do not exceed 250 words. In the event the
     * document is not accessible online, the description field can be used to describe arrangements for obtaining a
     * copy of the document.
     */
    @JsonProperty("description")
    @JsonPropertyDescription("A short description of the document. We recommend descriptions do not exceed 250 words."
            + " In the event the document is not accessible online, the description field can be used to describe "
            + "arrangements for obtaining a copy of the document.")
    private String description;
    /**
     * URL
     * <p>
     * direct link to the document or attachment. The server providing access to this document should be configured
     * to correctly report the document mime type.
     */
    @JsonProperty("url")
    @JsonPropertyDescription(" direct link to the document or attachment. The server providing access to this "
            + "document should be configured to correctly report the document mime type.")
    private URI url;
    /**
     * Date published
     * <p>
     * The date on which the document was first published. This is particularly important for legally important
     * documents such as notices of a tender.
     */
    @JsonProperty("datePublished")
    @JsonPropertyDescription("The date on which the document was first published. This is particularly important for "
            + "legally important documents such as notices of a tender.")
    private Date datePublished;
    /**
     * Date modified
     * <p>
     * Date that the document was last modified
     */
    @JsonProperty("dateModified")
    @JsonPropertyDescription("Date that the document was last modified")
    private Date dateModified;
    /**
     * Format
     * <p>
     * The format of the document taken from the [IANA Media Types codelist](http://www.iana
     * .org/assignments/media-types/), with the addition of one extra value for 'offline/print', used when this
     * document entry is being used to describe the offline publication of a document. Use values from the template
     * column. Links to web pages should be tagged 'text/html'.
     */
    @JsonProperty("format")
    @JsonPropertyDescription("The format of the document taken from the [IANA Media Types codelist](http://www.iana"
            + ".org/assignments/media-types/), with the addition of one extra value for 'offline/print', used when "
            + "this document entry is being used to describe the offline publication of a document. Use values from "
            + "the template column. Links to web pages should be tagged 'text/html'.")
    private String format;
    /**
     * Language
     * <p>
     * Specifies the language of the linked document using either two-letter [ISO639-1](https://en.wikipedia
     * .org/wiki/List_of_ISO_639-1_codes), or extended [BCP47 language tags](http://www
     * .w3.org/International/articles/language-tags/). The use of lowercase two-letter codes from [ISO639-1]
     * (https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) is strongly recommended unless there is a clear user
     * need for distinguishing the language subtype.
     */
    @JsonProperty("language")
    @JsonPropertyDescription("Specifies the language of the linked document using either two-letter [ISO639-1]"
            + "(https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes), or extended [BCP47 language tags](http://www"
            + ".w3.org/International/articles/language-tags/). The use of lowercase two-letter codes from [ISO639-1]"
            + "(https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) is strongly recommended unless there is a "
            + "clear user need for distinguishing the language subtype.")
    private String language;

    /**
     * ID
     * <p>
     * A local, unique identifier for this document. This field is used to keep track of multiple revisions of a
     * document through the compilation from release to record mechanism.
     * (Required)
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * ID
     * <p>
     * A local, unique identifier for this document. This field is used to keep track of multiple revisions of a
     * document through the compilation from release to record mechanism.
     * (Required)
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Document type
     * <p>
     * A classification of the document described taken from the [documentType codelist](http://standard
     * .open-contracting.org/latest/en/schema/codelists/#document-type). Values from the provided codelist should be
     * used wherever possible, though extended values can be provided if the codelist does not have a relevant code.
     */
    @JsonProperty("documentType")
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Document type
     * <p>
     * A classification of the document described taken from the [documentType codelist](http://standard
     * .open-contracting.org/latest/en/schema/codelists/#document-type). Values from the provided codelist should be
     * used wherever possible, though extended values can be provided if the codelist does not have a relevant code.
     */
    @JsonProperty("documentType")
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * Title
     * <p>
     * The document title.
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Title
     * <p>
     * The document title.
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Description
     * <p>
     * A short description of the document. We recommend descriptions do not exceed 250 words. In the event the
     * document is not accessible online, the description field can be used to describe arrangements for obtaining a
     * copy of the document.
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Description
     * <p>
     * A short description of the document. We recommend descriptions do not exceed 250 words. In the event the
     * document is not accessible online, the description field can be used to describe arrangements for obtaining a
     * copy of the document.
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * URL
     * <p>
     * direct link to the document or attachment. The server providing access to this document should be configured
     * to correctly report the document mime type.
     */
    @JsonProperty("url")
    public URI getUrl() {
        return url;
    }

    /**
     * URL
     * <p>
     * direct link to the document or attachment. The server providing access to this document should be configured
     * to correctly report the document mime type.
     */
    @JsonProperty("url")
    public void setUrl(URI url) {
        this.url = url;
    }

    /**
     * Date published
     * <p>
     * The date on which the document was first published. This is particularly important for legally important
     * documents such as notices of a tender.
     */
    @JsonProperty("datePublished")
    public Date getDatePublished() {
        return datePublished;
    }

    /**
     * Date published
     * <p>
     * The date on which the document was first published. This is particularly important for legally important
     * documents such as notices of a tender.
     */
    @JsonProperty("datePublished")
    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    /**
     * Date modified
     * <p>
     * Date that the document was last modified
     */
    @JsonProperty("dateModified")
    public Date getDateModified() {
        return dateModified;
    }

    /**
     * Date modified
     * <p>
     * Date that the document was last modified
     */
    @JsonProperty("dateModified")
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * Format
     * <p>
     * The format of the document taken from the [IANA Media Types codelist](http://www.iana
     * .org/assignments/media-types/), with the addition of one extra value for 'offline/print', used when this
     * document entry is being used to describe the offline publication of a document. Use values from the template
     * column. Links to web pages should be tagged 'text/html'.
     */
    @JsonProperty("format")
    public String getFormat() {
        return format;
    }

    /**
     * Format
     * <p>
     * The format of the document taken from the [IANA Media Types codelist](http://www.iana
     * .org/assignments/media-types/), with the addition of one extra value for 'offline/print', used when this
     * document entry is being used to describe the offline publication of a document. Use values from the template
     * column. Links to web pages should be tagged 'text/html'.
     */
    @JsonProperty("format")
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Language
     * <p>
     * Specifies the language of the linked document using either two-letter [ISO639-1](https://en.wikipedia
     * .org/wiki/List_of_ISO_639-1_codes), or extended [BCP47 language tags](http://www
     * .w3.org/International/articles/language-tags/). The use of lowercase two-letter codes from [ISO639-1]
     * (https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) is strongly recommended unless there is a clear user
     * need for distinguishing the language subtype.
     */
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    /**
     * Language
     * <p>
     * Specifies the language of the linked document using either two-letter [ISO639-1](https://en.wikipedia
     * .org/wiki/List_of_ISO_639-1_codes), or extended [BCP47 language tags](http://www
     * .w3.org/International/articles/language-tags/). The use of lowercase two-letter codes from [ISO639-1]
     * (https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) is strongly recommended unless there is a clear user
     * need for distinguishing the language subtype.
     */
    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("documentType", documentType)
                .append("title", title)
                .append("description", description)
                .append("url", url)
                .append("datePublished", datePublished)
                .append("dateModified", dateModified)
                .append("format", format)
                .append("language", language)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(datePublished)
                .append(documentType)
                .append(format)
                .append(description)
                .append(dateModified)
                .append(language)
                .append(id)
                .append(title)
                .append(url)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Document)) {
            return false;
        }
        Document rhs = ((Document) other);
        return new EqualsBuilder().append(datePublished, rhs.datePublished)
                .append(documentType, rhs.documentType)
                .append(format, rhs.format)
                .append(description, rhs.description)
                .append(dateModified, rhs.dateModified)
                .append(language, rhs.language)
                .append(id, rhs.id)
                .append(title, rhs.title)
                .append(url, rhs.url)
                .isEquals();
    }

}
