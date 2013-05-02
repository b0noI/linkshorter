package ua.in.link.rest;

/**
 * Created with IntelliJ IDEA.
 * User: b0noI
 * Date: 07.04.13
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public final class RESTSettings {

    private static final String NET_REST_URL = "http://link-in-ua.j.rsnx.ru/test";

    private static final String LOCAL_REST_URL = "http://localhost:8080";

    private static boolean isLocaleMode = true;

    public static String getRestUrl() {
        return isLocaleMode ? LOCAL_REST_URL : NET_REST_URL;
    }

    public static void setLocaleMode(boolean localeMode) {
        isLocaleMode = localeMode;
    }

}
