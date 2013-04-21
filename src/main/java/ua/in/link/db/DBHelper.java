package ua.in.link.db;

import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.UpdateResults;
import com.google.gson.Gson;
import com.mongodb.*;

import ua.in.link.db.ip.IPData;
import ua.in.link.db.ip.IPRepository;
import ua.in.link.utils.RandomString;

import java.net.UnknownHostException;
import java.util.*;

import org.apache.commons.lang.time.DateUtils;

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

    private final Morphia morphia = new Morphia();

    private final IPRepository ipRepository;

    private final MongoClient mongo;

    private DBHelper() {
        try {
            mongo = new MongoClient(IDBSettings.DB_URL, IDBSettings.DB_PORT);
            DB db = mongo.getDB(IDBSettings.DB_NAME);
            db.authenticate(IDBSettings.DB_LOGIN, IDBSettings.DB_PASSWORD);
            urls = db.getCollection(IDBSettings.COLLECTION_NAME);
            ipRepository = new IPRepository(mongo, morphia, IDBSettings.DB_NAME);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static DBHelper getInstance() {
        return InstanceHolder.getInstance();
    }

    public URLData getShortUrl(String fullUrl) {
        URLData urlFromDB = checkFullUrl(fullUrl);
        if (urlFromDB != null)
            return urlFromDB;

        URLData url = new URLData(fullUrl, generateNewShort(), new Date(), new ArrayList<URLData.DataStat>());
        BasicDBObject urlDBObject = new BasicDBObject(IDBSettings.URL_FILED_NAME, url.getOriginalUrl()).
                append(IDBSettings.SHORT_CODE_FILED_NAME, url.getShortUrl()).
                append(IDBSettings.CREATION_TIME_FILED_NAME, url.getCreationTime().getTime()).
                append(IDBSettings.STATISTIC_FILED_NAME, GSON.toJson(url.getStatistic()));
        urls.insert(urlDBObject);
        return url;
    }

    public URLData getFullUrl(String shortUrl) {
        try(DBCursor c = urls.find(new BasicDBObject(IDBSettings.SHORT_CODE_FILED_NAME, shortUrl))){
            if (!c.hasNext())
                return null;
            DBObject object = c.next();
            String statJson = (String)object.get(IDBSettings.STATISTIC_FILED_NAME);

            List<URLData.DataStat> stat = new ArrayList<>();
            if (statJson != null)
                stat = GSON.fromJson(statJson, List.class);
            Long creationTimeLong = (Long)object.get(IDBSettings.CREATION_TIME_FILED_NAME);
            if (creationTimeLong == null)
                creationTimeLong = new Date().getTime();
            return new URLData((String)object.get(IDBSettings.URL_FILED_NAME), shortUrl,
                    new Date(creationTimeLong),
                    stat);
        }
    }

    public void incrementStatForURL(URLData url, String country, String OS) {
        DBObject c = urls.findOne(new BasicDBObject(IDBSettings.SHORT_CODE_FILED_NAME, url.getShortUrl()));
        List<URLData.DataStat> stats = (List<URLData.DataStat>)GSON.fromJson((String) c.get(IDBSettings.STATISTIC_FILED_NAME), List.class);
        if (stats == null)
            stats = new ArrayList<>();
        URLData.DataStat statData = new URLData.DataStat(new Date(), country, OS);
        stats.add(statData);
        BasicDBObject newObject = new BasicDBObject(c.toMap()).append(IDBSettings.STATISTIC_FILED_NAME, GSON.toJson(stats));
        urls.update(c, newObject);
    }

    private URLData checkFullUrl(String fullUrl) {
        try(DBCursor c = urls.find(new BasicDBObject(IDBSettings.URL_FILED_NAME, fullUrl))){
            if (!c.hasNext())
                return null;
            DBObject object = c.next();

            String statJson = (String)object.get(IDBSettings.STATISTIC_FILED_NAME);
            List<URLData.DataStat> stat = new ArrayList<>();
            if (statJson != null)
                stat = GSON.fromJson(statJson, List.class);
            Long creationTimeLong = (Long)object.get(IDBSettings.CREATION_TIME_FILED_NAME);
            if (creationTimeLong == null)
                creationTimeLong = new Date().getTime();
            return new URLData(fullUrl, (String)object.get(IDBSettings.SHORT_CODE_FILED_NAME),
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
     * @return unique short string URLData
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

    public void checkIP(String ip) throws IllegalAccessException {
      Date now = new Date();

      IPData ipDataByDay = ipRepository.findOne(ipRepository.createQuery().field("ip").equal(ip).
              field("interval").equal(IPData.Interval.DAY).field("date").greaterThanOrEq(DateUtils.addDays(now, -1)));
      IPData updateIPInterval = updateIPInterval(ipDataByDay, ip, IPData.Interval.DAY, now);
      checkIPData(updateIPInterval);

      IPData ipDataByHour = ipRepository.findOne(ipRepository.createQuery().field("ip").equal(ip).
              field("interval").equal(IPData.Interval.HOUR).field("date").greaterThanOrEq(DateUtils.addHours(now, -1)));
      IPData updateIPInterval2 = updateIPInterval(ipDataByHour, ip, IPData.Interval.HOUR, now);
      checkIPData(updateIPInterval2);

      IPData ipDataByMinute = ipRepository.findOne(ipRepository.createQuery().field("ip").equal(ip).
              field("interval").equal(IPData.Interval.MINUTE).field("date").greaterThanOrEq(DateUtils.addMinutes(now, -1)));
      IPData updateIPInterval3 = updateIPInterval(ipDataByMinute, ip, IPData.Interval.MINUTE, now);
      checkIPData(updateIPInterval3);

      IPData ipDataBySecond = ipRepository.findOne(ipRepository.createQuery().field("ip").equal(ip).
              field("interval").equal(IPData.Interval.SECOND).field("date").greaterThanOrEq(DateUtils.addSeconds(now, -1)));
      IPData updateIPInterval4 = updateIPInterval(ipDataBySecond, ip, IPData.Interval.SECOND, now);
      checkIPData(updateIPInterval4);
    }

    private void checkIPData(IPData data) throws IllegalAccessException {
        if (data.getCount() > data.getInterval().getPermittedNumber()) {
            throw new IllegalAccessException("Exceeded the "+data.getInterval().name()+" query limit. Expect: " + data.getInterval().getPermittedNumber());
        }

    }

    private IPData updateIPInterval(IPData data, String ip, IPData.Interval interval, Date date) {
        if (data == null) {
            data = new IPData();
            data.setIp(ip);
            data.setDate(date);
            data.setCount(1);
            data.setInterval(interval);
            ipRepository.save(data);

            return data;
        } else {
            ipRepository.updateFirst(ipRepository.createQuery().field("_id").equal(data.getId()), ipRepository.createUpdateOperations().inc("count", 1));
            return ipRepository.findOne(ipRepository.createQuery().field("_id").equal(data.getId()));
        }
    }

    private static class InstanceHolder {

        private static DBHelper INSTANCE = new DBHelper();

        public static DBHelper getInstance() {
            return INSTANCE;
        }

    }

}
