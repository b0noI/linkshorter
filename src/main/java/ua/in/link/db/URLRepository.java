package ua.in.link.db;

import org.bson.types.ObjectId;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;

public class URLRepository extends BasicDAO<URLData, ObjectId>  {

    public URLRepository(Mongo mongo, Morphia morphia, String dbName) {
        super(mongo, morphia, dbName);
    }

}
