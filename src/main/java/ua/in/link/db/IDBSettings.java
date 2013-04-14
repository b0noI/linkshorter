package ua.in.link.db;

/**
 * DB settings.
 * User: b0noI
 * Date: 07.04.13
 * Time: 16:42
 */
interface IDBSettings {

    String DB_URL = "mongodb-dateme.j.rsnx.ru";

    int DB_PORT = 27017;

    String DB_NAME = "links";

    String COLLECTION_NAME = "urls";

    String URL_FILED_NAME = "url";

    String CREATION_TIME_FILED_NAME = "creation_time";

    String STATISTIC_FILED_NAME = "statistics";

    String SHORT_CODE_FILED_NAME = "short";

    String DB_LOGIN = "linker_prod";

    char[] DB_PASSWORD = {'t'};

}
