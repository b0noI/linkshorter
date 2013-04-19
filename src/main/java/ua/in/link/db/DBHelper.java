package ua.in.link.db;

import com.google.gson.Gson;
import com.mongodb.*;
import org.junit.Test;
import ua.in.link.utils.RandomString;

import java.net.UnknownHostException;
import java.util.*;

/**
 * The DB helper.
 * User: b0noI
 * Date: 06.04.13
 * Time: 23:18
 */
public class DBHelper {

    private static final int RANDOM_STRING_LENGTH = 5;

    private static final RandomString RANDOM_STRING = new RandomString(RANDOM_STRING_LENGTH);

    private static final Gson GSON = new Gson();

    private final DBCollection urls;

    private final MongoClient mongo;

    private DBHelper() {
        try {
            mongo = new MongoClient(IDBSettings.DB_URL, IDBSettings.DB_PORT);
            DB db = mongo.getDB(IDBSettings.DB_NAME);
            db.authenticate(IDBSettings.DB_LOGIN, IDBSettings.DB_PASSWORD);
            urls = db.getCollection(IDBSettings.COLLECTION_NAME);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static DBHelper getInstance() {
        return InstanceHolder.getInstance();
    }

    public URL getShortUrl(String fullUrl) {
        URL urlFromDB = checkFullUrl(fullUrl);
        if (urlFromDB != null)
            return urlFromDB;

        URL url = new URL(fullUrl, generateNewShort(), new Date(), new ArrayList<URL.DataStat>());
        BasicDBObject urlDBObject = new BasicDBObject(IDBSettings.URL_FILED_NAME, url.getOriginalUrl()).
                append(IDBSettings.SHORT_CODE_FILED_NAME, url.getShortUrl()).
                append(IDBSettings.CREATION_TIME_FILED_NAME, url.getCreationTime().getTime()).
                append(IDBSettings.STATISTIC_FILED_NAME, GSON.toJson(url.getStatistic()));
        urls.insert(urlDBObject);
        return url;
    }

    public URL getFullUrl(String shortUrl) {
        try(DBCursor c = urls.find(new BasicDBObject(IDBSettings.SHORT_CODE_FILED_NAME, shortUrl))){
            if (!c.hasNext())
                return null;
            DBObject object = c.next();
            String statJson = (String)object.get(IDBSettings.STATISTIC_FILED_NAME);

            List<URL.DataStat> stat = new ArrayList<>();
            if (statJson != null)
                stat = GSON.fromJson(statJson, List.class);
            Long creationTimeLong = (Long)object.get(IDBSettings.CREATION_TIME_FILED_NAME);
            if (creationTimeLong == null)
                creationTimeLong = new Date().getTime();
            return new URL((String)object.get(IDBSettings.URL_FILED_NAME), shortUrl,
                    new Date(creationTimeLong),
                    stat);
        }
    }

    public void incrementStatForURL(URL url, String OS) {
        DBObject c = urls.findOne(new BasicDBObject(IDBSettings.SHORT_CODE_FILED_NAME, url.getShortUrl()));
        List<URL.DataStat> stats = (List<URL.DataStat>)GSON.fromJson((String) c.get(IDBSettings.STATISTIC_FILED_NAME), List.class);
        if (stats == null)
            stats = new ArrayList<>();
        URL.DataStat statData = new URL.DataStat(new Date(), OS);
        stats.add(statData);
        BasicDBObject newObject = new BasicDBObject(c.toMap()).append(IDBSettings.STATISTIC_FILED_NAME, GSON.toJson(stats));
        urls.update(c, newObject);
    }

    private URL checkFullUrl(String fullUrl) {
        try(DBCursor c = urls.find(new BasicDBObject(IDBSettings.URL_FILED_NAME, fullUrl))){
            if (!c.hasNext())
                return null;
            DBObject object = c.next();

            String statJson = (String)object.get(IDBSettings.STATISTIC_FILED_NAME);
            List<URL.DataStat> stat = new ArrayList<>();
            if (statJson != null)
                stat = GSON.fromJson(statJson, List.class);
            Long creationTimeLong = (Long)object.get(IDBSettings.CREATION_TIME_FILED_NAME);
            if (creationTimeLong == null)
                creationTimeLong = new Date().getTime();
            return new URL(fullUrl, (String)object.get(IDBSettings.SHORT_CODE_FILED_NAME),
                    new Date(creationTimeLong),
                    stat);
        }
    }

    /**
     * It's significantly faster to use find() + limit() because findOne()
     * will always read + return the document if it exists. find() just returns
     * a cursor (or not) and only reads the data if you iterate through the cursor
     *
     * If tryRandomShortUrl already exists in our DB (count = 1), it's automatically
     * generate another string, until it would be unique (count != 1).
     *
     * @return unique short string URL
     */
    private String generateNewShort() {

        String uniqueShortUrl = null;
        String tryRandomShortUrl;
        DBCursor cursor;
        boolean UrlAlreadyExist = true;

        do {
            tryRandomShortUrl = RANDOM_STRING.nextString();
            cursor = urls.find(new BasicDBObject(
                    IDBSettings.SHORT_CODE_FILED_NAME, tryRandomShortUrl)).limit(1);
            int count = cursor.count();

            if (count != 1) {
                uniqueShortUrl = tryRandomShortUrl;
                UrlAlreadyExist = false;
            }

        } while (UrlAlreadyExist);

        return uniqueShortUrl;

    }

    private static class InstanceHolder {

        private static DBHelper INSTANCE = new DBHelper();

        public static DBHelper getInstance() {
            return INSTANCE;
        }

    }

}
