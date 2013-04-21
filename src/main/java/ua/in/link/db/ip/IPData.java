package ua.in.link.db.ip;

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Transient;

/**
 *
 * @author odis
 * 
 */
@Entity("ips")
public class IPData {

    @Id
    private ObjectId id;

    @Indexed
    private String ip;

    private Date date;

    private int count;

    private Interval interval;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public enum Interval {
        SECOND(3),
        MINUTE(10),
        HOUR(30),
        DAY(100);

        private final long PERMITTED_NUMBER;

        private Interval(long PERMITTED_NUMBER){
            this.PERMITTED_NUMBER = PERMITTED_NUMBER;
        }

        public long getPermittedNumber() {
            return PERMITTED_NUMBER;
        }
    }

}
