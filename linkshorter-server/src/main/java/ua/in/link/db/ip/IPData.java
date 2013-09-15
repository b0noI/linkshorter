package ua.in.link.db.ip;

import com.google.code.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 *
 * @author odis
 *
 */
@Entity(IPData.IP_COLLECTION_NAME)
public class IPData {
	
    @Transient
    public final static String IP_COLLECTION_NAME = "ips";
    @Transient
    public final static String IP_FILED_NAME = "ip";
    @Transient
    public final static String CREATION_TIME_FILED_NAME = "creationTime";
    @Transient
    public final static String REQUEST_COUNT_FILED_NAME = "requestCount";

    @Id
    private ObjectId id;

    @Indexed
    @Property(IP_FILED_NAME)
    private String ip;

    @Indexed
    @Property(CREATION_TIME_FILED_NAME)
    private Date date;

    @Property(REQUEST_COUNT_FILED_NAME)
    private Integer count;

    public IPData() {
    }

    public IPData(String ip, Date date, Integer count) {
        this.ip = ip;
        this.date = date;
        this.count = count;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
