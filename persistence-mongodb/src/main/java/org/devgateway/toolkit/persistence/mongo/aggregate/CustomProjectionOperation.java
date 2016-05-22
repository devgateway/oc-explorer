/**
 *
 */
package org.devgateway.toolkit.persistence.mongo.aggregate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author mihai
 *
 */
public class CustomProjectionOperation extends CustomOperation {

    /**
     * @param operation
     */
    public CustomProjectionOperation(DBObject operation) {
        super(new BasicDBObject("$project", operation));
    }

}
