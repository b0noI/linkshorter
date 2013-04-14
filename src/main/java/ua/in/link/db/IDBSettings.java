package ua.in.link.db;

/**
 * Created with IntelliJ IDEA.
 * User: b0noI
 * Date: 07.04.13
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
interface IDBSettings {

    String DB_URL = "mongodb-dateme.j.rsnx.ru";

    int DB_PORT = 27017;

    String DB_NAME = "links";

    String COLLECTIO_NAME = "urls";

    String URL_FILED_NAME = "url";

    String CREATION_TIME_FILED_NAME = "creation_time";

    String STATISTIC_FILED_NAME = "statistics";

    String SHORT_CODE_FILED_NAME = "short";

    String DB_LOGIN = "linker_prod";

    char[] DB_PASSWORD = {'t'};

}
