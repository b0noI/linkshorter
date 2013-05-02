package ua.in.link.db;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;

import ua.in.link.db.ip.IPData;
import ua.in.link.db.ip.IPRepository;

import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * The DB helper.
 * User: b0noI
 * Date: 06.04.13
 * Time: 23:18
 */
public class DBHelper {

    private static final String SERVER_IP = "89.253.237.43";

    private final Lock lock = new ReentrantLock();
    private static final int RANDOM_STRING_LENGTH = 5;

    private final Morphia morphia = new Morphia();
    private final IPRepository ipRepository;
    private final URLRepository urlRepository;

    private final MongoClient mongo;

    private DBHelper() {
        try {
            mongo = new MongoClient(IDBSettings.DB_URL, IDBSettings.DB_PORT);
            DB db = mongo.getDB(IDBSettings.DB_NAME);
            db.authenticate(IDBSettings.DB_LOGIN, IDBSettings.DB_PASSWORD);
            ipRepository = new IPRepository(mongo, morphia, IDBSettings.DB_NAME);
            urlRepository = new URLRepository(mongo, morphia, IDBSettings.DB_NAME);
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
            URLData url = new URLData(fullUrl, generateNewShort(), new Date(),
                    new ArrayList<URLData.DataStat>());
            urlRepository.save(url);
            return url;
        } finally {
            lock.unlock();
        }
    }

    public void incrementStatForURL(URLData url, String country, String OS) {
        URLData.DataStat value = new URLData.DataStat(new Date(), country, OS);
        urlRepository.update(urlRepository.createQuery().field(IDBSettings.ID_FIELD_NAME).equal(url.getId()), urlRepository.createUpdateOperations().add(URLData.STATISTIC_FILED_NAME, value));
    }

    public URLData getFullUrl(String shortUrl) {
        return urlRepository.findOne(URLData.SHORT_CODE_FILED_NAME, shortUrl);
    }

    private String generateNewShort() {
        String random = RandomStringUtils.random(RANDOM_STRING_LENGTH, true, true);
        return urlRepository.count(urlRepository.createQuery().field(URLData.SHORT_CODE_FILED_NAME).equal(random)) > 0 ? generateNewShort() : random;
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
          if (!ip.equals(SERVER_IP))
            checkIPData(updatedIpData.getCount(), Interval.SECOND);
      }

      for(Interval interval: Interval.values()) {
          if (interval == Interval.SECOND){
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


    private URLData checkFullUrl(String fullUrl) {
        return urlRepository.findOne(URLData.URL_FILED_NAME, fullUrl);
    }

    private void checkIPData(long count, Interval interval) throws IllegalAccessException {
        if (count > interval.getPermittedNumber()) {
            throw new IllegalAccessException("Exceeded the "+interval.name()+" query limit. Expect: " + interval.getPermittedNumber());
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
