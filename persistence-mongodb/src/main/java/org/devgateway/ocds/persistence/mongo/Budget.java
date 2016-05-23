/**
 *
 */
package org.devgateway.ocds.persistence.mongo;

/**
 * @author mihai
 * Budget OCDS Entity http://standard.open-contracting.org/latest/en/schema/reference/#budget
 */

public class Budget {
    private String source;

    private String id;

    private String description;

    private Value amount;

    private String project;

    private String projectID;

    private String uri;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Value getAmount() {
        return amount;
    }

    public void setAmount(Value amount) {
        this.amount = amount;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


}
