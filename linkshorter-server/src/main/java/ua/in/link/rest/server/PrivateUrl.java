package ua.in.link.rest.server;

/**
 *
 * User: ivan.mushketyk at gmail.com
 */
public class PrivateUrl {

    private String url;

    private String password;

    public String getLongUrl() {
        return url;
    }

    public void setLongUrl(String longUrl) {
        this.url = longUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
