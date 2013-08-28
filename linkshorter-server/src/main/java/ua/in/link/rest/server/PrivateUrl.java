package ua.in.link.rest.server;

/**
 *
 * User: ivan.mushketyk at gmail.com
 */
public class PrivateUrl {

    private String longUrl;

    private String password;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
