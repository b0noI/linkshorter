package ua.in.link.db;

import com.google.code.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * The url class.
 * User: b0noI
 * Date: 06.04.13
 * Time: 23:55
 */
@Entity(URLData.IP_COLLECTION_NAME)
public class URLData {
	
	@Transient
    public final static String IP_COLLECTION_NAME = "urls";
    @Transient
    public static final String URL_FILED_NAME = "url";
    @Transient
    public static final String CREATION_TIME_FILED_NAME = "creationTime";
    @Transient
    public static final String STATISTIC_FILED_NAME = "statistics";
    @Transient
    public static final String SHORT_CODE_FILED_NAME = "short";
    @Transient
    public static final String PASSWORD_FIELD_NAME = "password";

    @Id
    private ObjectId id;

    @Indexed
    @Property(URL_FILED_NAME)
    private String originalUrl;

    @Indexed
    @Property(SHORT_CODE_FILED_NAME)
    private String shortUrl;

    @Property(CREATION_TIME_FILED_NAME)
    private Date creationTime;

    @Property(STATISTIC_FILED_NAME)
    private List<DataStat> statistics;

    @Property(PASSWORD_FIELD_NAME)
    private String password = null;

    public URLData() {
    }

    URLData(String original, String shortUrl, Date creationTime, List<DataStat> statistics) {
        this.originalUrl = original;
        this.shortUrl = shortUrl;
        this.creationTime = creationTime;
        this.statistics = statistics;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public List<DataStat> getStatistic() {
        return statistics;
    }

    public void setStatistic(List<DataStat> statistics) {
        this.statistics = statistics;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Embedded
    public static class DataStat {

        @Transient
        public static final String OPEN_DATE_FILED_NAME = "openDate";
        @Transient
        public static final String COUNTRY_FILED_NAME = "country";
        @Transient
        public static final String OS_FILED_NAME = "os";

        @Indexed
        private Date openDate;

        @Indexed
        private String country;

        @Indexed
        private String os;

        public DataStat() {

        }

        public DataStat(Date openDate, String country, String os) {
            this.openDate = openDate;
            this.country = country;
            this.os = os;
        }

        public Date getOpenDate() {
            return openDate;
        }

        public void setOpenDate(Date openDate) {
            this.openDate = openDate;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

    }

}
