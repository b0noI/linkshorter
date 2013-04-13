package ua.in.link.db;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: b0noI
 * Date: 06.04.13
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
public class URL {

    private final String ORIGINAL_URL;

    private final String SHORT_URL;

    private final Date CREATION_TIME;

    private final List<DataStat> STATISTIC;

    URL(String original, String shortUrl, Date creationTime, List<DataStat> statistic) {
        ORIGINAL_URL = original;
        SHORT_URL = shortUrl;
        CREATION_TIME = creationTime;
        STATISTIC = statistic;
    }

    public List<DataStat> getStatistic() {
        return STATISTIC;
    }

    public String getOriginalUrl() {
        return ORIGINAL_URL;
    }

    public String getShortUrl() {
        return SHORT_URL;
    }

    public Date getCreationTime() {
        return CREATION_TIME;
    }

    public static class DataStat {

        private final Date OPEN_DATE;

        private final String COUNTRY;

        private final String OS;

        public DataStat(Date openDate, String country, String OS) {
            this.OPEN_DATE = openDate;
            this.COUNTRY = country;
            this.OS = OS;
        }

    }

}
