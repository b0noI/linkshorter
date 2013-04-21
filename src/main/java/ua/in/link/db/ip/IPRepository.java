/**
 *
 */
package ua.in.link.db.ip;

import org.bson.types.ObjectId;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;

/**
 * @author odis
 *
 */
public class IPRepository extends BasicDAO<IPData, ObjectId>{

    public IPRepository(Mongo mongo, Morphia morphia, String dbName) {
        super(mongo, morphia, dbName);
        // TODO Auto-generated constructor stub
    }

}
