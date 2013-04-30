package ua.in.link.db;

/**
 * DB settings.
 * User: b0noI
 * Date: 07.04.13
 * Time: 16:42
 */
public interface IDBSettings {

    String DB_URL = "mongodb-dateme.j.rsnx.ru";

    int DB_PORT = 27017;
    
    String ID_FIELD_NAME = "_id";

    String DB_NAME = "links";

    String DB_LOGIN = "linker_prod";

    char[] DB_PASSWORD = {'t'};

}
