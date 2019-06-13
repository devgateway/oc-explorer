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
public class CustomGroupingOperation extends CustomOperation {

    /**
     * @param operation
     */
    public CustomGroupingOperation(final DBObject operation) {
        super(new Document("$group", operation));
    }

}
