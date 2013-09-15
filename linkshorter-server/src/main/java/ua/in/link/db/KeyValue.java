package ua.in.link.db;

import com.google.code.morphia.annotations.*;

/**
 *
 * User: ivan.mushketyk at gmail.com
 */
@Entity(KeyValue.KEY_VALUE_COLLECTON_NAME)
public class KeyValue {

    @Transient
    public final static String KEY_VALUE_COLLECTON_NAME = "keyValues";

    @Transient
    public final static String KEY_FIELD_NAME = "key";

    @Transient
    public final static String VALUE_FIELD_NAME = "value";

    @Id
    @Indexed
    @Property(KEY_FIELD_NAME)
    private String key;

    @Indexed
    @Property(VALUE_FIELD_NAME)
    private String value;

    public KeyValue() {
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
