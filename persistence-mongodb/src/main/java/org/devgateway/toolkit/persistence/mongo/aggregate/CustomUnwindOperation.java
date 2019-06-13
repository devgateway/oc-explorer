/**
 *
 */
package org.devgateway.toolkit.persistence.mongo.aggregate;

import com.mongodb.BasicDBObject;
import org.bson.Document;

/**
 * @author mpostelnicu
 */
public class CustomUnwindOperation extends CustomOperation {

    public CustomUnwindOperation(final String field) {
        super(new Document("$unwind", field));
    }

    public CustomUnwindOperation(final String field, boolean preserveNullAndEmptyArrays) {
        super(new Document("$unwind",
                new Document("path", field).append("preserveNullAndEmptyArrays", preserveNullAndEmptyArrays)));
    }

}
