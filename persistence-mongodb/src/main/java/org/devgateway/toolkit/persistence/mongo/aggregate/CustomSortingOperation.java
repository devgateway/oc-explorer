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
public class CustomSortingOperation extends CustomOperation {

    /**
     * @param operation
     */
    public CustomSortingOperation(final DBObject operation) {
        super(new Document("$sort", operation));
    }

}
