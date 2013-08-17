package ua.in.link.db;

import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.apache.commons.lang.time.DateUtils;
import ua.in.link.db.ip.IPData;
import ua.in.link.db.ip.IPRepository;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * The DB helper.
 * User: b0noI
 * Date: 06.04.13
 * Time: 23:18
 */
public class DBHelper {

    private static final    String          SERVER_IP               = "89.253.237.43";

    private static final    String          SERVER_IP2               = "188.231.145.181";

    private static final    String          LOCAL_HOST_STR          = "localhost";

    private static final    String          LOCAL_HOST_IP           = "127.0.0.1";

    private static final    int             RANDOM_STRING_LENGTH    = 5;

    private static final    String          LOGGER_NAME             = "ErverLogger";

    private static final    Logger          LOGGER                  = Logger.getLogger(LOGGER_NAME);

    private        final    Lock            lock                    = new ReentrantLock();

    private        final    Morphia         morphia                 = new Morphia();

    private        final    IPRepository    ipRepository;

    private        final    URLRepository   urlRepository;
    private        final    KeyValueRepository keyValueRepository;

    private        final    MongoClient     mongo;

    private                 boolean         ignoreLocalHost         = false;

    private DBHelper() {
        try {
            mongo = new MongoClient(IDBSettings.DB_URL, IDBSettings.DB_PORT);
            DB db = mongo.getDB(IDBSettings.DB_NAME);
            db.authenticate(IDBSettings.DB_LOGIN, IDBSettings.DB_PASSWORD);
            ipRepository = new IPRepository(mongo, morphia, IDBSettings.DB_NAME);
            urlRepository = new URLRepository(mongo, morphia, IDBSettings.DB_NAME);
            keyValueRepository = new KeyValueRepository(mongo, morphia, IDBSettings.DB_NAME);

            initNextShortUrl();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public URLData getShortUrl(String fullUrl) {
        URLData urlFromDB = checkFullUrl(fullUrl);
        if (urlFromDB != null)
            return urlFromDB;

        lock.lock();
        try {
            String newShortURL = generateNewShort();
            URLData url = new URLData(fullUrl, newShortURL, new Date(),
                    new ArrayList<URLData.DataStat>());
            saveNewNextURL(NextURLUtils.getNextUrl(newShortURL));
            urlRepository.save(url);
            return url;
        } finally {
            lock.unlock();
        }
    }

    private void saveNewNextURL(String nextShortURL) {
        KeyValue keyValue = keyValueRepository.findOne(KeyValue.KEY_FIELD_NAME, KeyValueRepository.NEXT_SHORT_URL_KEY);
        keyValue.setValue(nextShortURL);
        keyValueRepository.updateFirst(keyValueRepository.createQuery().field(KeyValue.KEY_FIELD_NAME).equal(KeyValueRepository.NEXT_SHORT_URL_KEY),
                keyValueRepository.createUpdateOperations().set(KeyValue.VALUE_FIELD_NAME, nextShortURL));

    }

    public void incrementStatForURL(URLData url, String country, String OS) {
        URLData.DataStat value = new URLData.DataStat(new Date(), country, OS);
        try {
            urlRepository.update(urlRepository.createQuery().field(IDBSettings.ID_FIELD_NAME).equal(url.getId()), urlRepository.createUpdateOperations().add(URLData.STATISTIC_FILED_NAME, value));
        } catch (MongoException e) {
            // This exception is throwing on old links (version 1.0)
            e.printStackTrace();
        }
    }

    public URLData getFullUrl(String shortUrl) {
        return urlRepository.findOne(URLData.SHORT_CODE_FILED_NAME, shortUrl);
    }

    private String generateNewShort() {
        KeyValue kv =  keyValueRepository.findOne(KeyValue.KEY_FIELD_NAME, KeyValueRepository.NEXT_SHORT_URL_KEY);
        String newShortURL = kv.getValue();

        while (containsShortURL(newShortURL)) {
            newShortURL = NextURLUtils.getNextUrl(newShortURL);
        }

        return  newShortURL;
    }

    private boolean containsShortURL(String shortURL) {
        return urlRepository.count(urlRepository.createQuery().field(URLData.SHORT_CODE_FILED_NAME).equal(shortURL)) > 0;
    }


    public void checkIP(String ip) throws IllegalAccessException {
        Date now = new Date();
        IPData ipData = ipRepository.findOne(ipRepository.createQuery().field(IPData.IP_FILED_NAME).equal(ip).
                field(IPData.CREATION_TIME_FILED_NAME).greaterThanOrEq(DateUtils.addSeconds(now, Interval.SECOND.getInterval_Seconds())));
        if (ipData == null) {
            IPData data = new IPData(ip, now, 1);
            ipRepository.save(data);
        } else {
            ipRepository.updateFirst(ipRepository.createQuery().field(IDBSettings.ID_FIELD_NAME).equal(ipData.getId()), ipRepository.createUpdateOperations().inc(IPData.REQUEST_COUNT_FILED_NAME, 1));
            IPData updatedIpData = ipRepository.findOne(ipRepository.createQuery().field(IDBSettings.ID_FIELD_NAME).equal(ipData.getId()));
            if (!ignoreLocalHost && !isRequestFromServer(ip)) {
                try {
                    checkIPData(updatedIpData.getCount(), Interval.SECOND);
                } catch (IllegalAccessException e) {
                    LOGGER.info("IP limits exception: " + ip);
                    throw e;
                }
            }
        }

        if (!ignoreLocalHost && isRequestFromServer(ip))
            return;

        for (Interval interval : Interval.values()) {
            if (interval == Interval.SECOND) {
                continue;
            }
            List<IPData> asList = ipRepository.find(ipRepository.createQuery().field(IPData.IP_FILED_NAME).equal(ip).field(IPData.CREATION_TIME_FILED_NAME).
                    greaterThanOrEq(DateUtils.addSeconds(now, interval.getInterval_Seconds()))).asList();

            //Morphia does not support MongoDB aggregation yet.
            long count = 0;

            for (IPData data : asList) {
                count += data.getCount();
            }

            checkIPData(count, interval);
        }
    }

    public void removeShortUrl(String shortURL) {
        if (shortURL == null) {
            return ;
        }

        URLData urlData = getFullUrl(shortURL);
        if (urlData != null) {
            urlRepository.delete(urlData);
            KeyValue keyValue = keyValueRepository.findOne(KeyValue.KEY_FIELD_NAME, KeyValueRepository.NEXT_SHORT_URL_KEY);
            String nextURL = keyValue.getValue();
            if (shortURL.compareTo(nextURL) <= 0) {
                saveNewNextURL(shortURL);
            }
        }
        //urlRepository.deleteByQuery(urlRepository.createQuery().field(URLData.SHORT_CODE_FILED_NAME).equal(shortURL));
    }

    void setIgnoreLocalHost(boolean ignoreLocalHost) {
        this.ignoreLocalHost = ignoreLocalHost;
    }

    private boolean isRequestFromServer(String ip) {
        return ip.equals(SERVER_IP) || ip.contains(SERVER_IP) ||
                ip.equals(SERVER_IP2) || ip.contains(SERVER_IP2) ||
                isRequestFromLocalHost(ip);
    }

    private boolean isRequestFromLocalHost(String ip) {
        return ip.equals(LOCAL_HOST_STR) || ip.contains(LOCAL_HOST_STR) ||
                ip.equals(LOCAL_HOST_IP) || ip.contains(LOCAL_HOST_IP);
    }

    private URLData checkFullUrl(String fullUrl) {
        return urlRepository.findOne(URLData.URL_FILED_NAME, fullUrl);
    }

    private void checkIPData(long count, Interval interval) throws IllegalAccessException {
        if (count > interval.getPermittedNumber()) {
            throw new IllegalAccessException("Exceeded the " + interval.name() + " query limit. Expect: " + interval.getPermittedNumber());
        }
    }
    private void initNextShortUrl() {
        KeyValue keyValue = keyValueRepository.findOne(KeyValue.KEY_FIELD_NAME, KeyValueRepository.NEXT_SHORT_URL_KEY);
        if (keyValue == null) {
            keyValue = new KeyValue(KeyValueRepository.NEXT_SHORT_URL_KEY, KeyValueRepository.INIT_SHORT_URL);
            keyValueRepository.save(keyValue);
        }
    }

    private static class InstanceHolder {

        private static DBHelper INSTANCE = new DBHelper();

        public static DBHelper getInstance() {
            return INSTANCE;
        }

    }

    public static DBHelper getInstance() {
        return InstanceHolder.getInstance();
    }

}
