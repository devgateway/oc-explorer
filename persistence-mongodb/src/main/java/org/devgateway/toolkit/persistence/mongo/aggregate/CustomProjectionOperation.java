/**
 *
 */
package org.devgateway.toolkit.persistence.mongo.aggregate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;

/**
 * @author mpostelnicu
 *
 */
public class CustomProjectionOperation extends CustomOperation {

    /**
     * @param operation
     */
    public CustomProjectionOperation(final DBObject operation) {
        super(new Document("$project", operation));
    }

}
