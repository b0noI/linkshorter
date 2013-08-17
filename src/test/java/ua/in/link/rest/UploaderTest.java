package ua.in.link.rest;

import org.junit.Test;
import ua.in.link.db.DBHelper;
import ua.in.link.db.URLData;
import ua.in.link.rest.client.URLClient;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Uploader test.
 * User: b0noI
 * Date: 07.04.13
 * Time: 17:07
 */
public class UploaderTest extends BaseRestTest{

    private static final String SHORT_KEY_1_0_VERSION = "5t9a2";

    DBHelper dbHelper = DBHelper.getInstance();

    @Test
    public void testPostUrl() throws Exception {

        String shortUrl = null;

        try {
            String fullUrl = "http://test144.in.ua";
            shortUrl = URLClient.postUrlToRest(fullUrl);
            assertNotNull(shortUrl);
            URLClient.getFullUrl(shortUrl);
        } finally {
            dbHelper.removeShortUrl(shortUrl);
        }
    }

    @Test
    public void testGetUrl() throws Exception {

        String shortUrl = null;

        try {
            String fullUrl = "http://dateme222.in.ua";
            shortUrl = URLClient.postUrlToRest(fullUrl);
            String fromServer = URLClient.getFullUrl(shortUrl);
            assertEquals("<html><head><meta HTTP-EQUIV=\"REFRESH\" content=\"0; url=http://dateme222.in.ua\"></head></html>", fromServer);
        } finally {
            dbHelper.removeShortUrl(shortUrl);
        }
    }

    @Test
    public void testStat() throws Exception {
        String shortUrl = null;

        try {
            String fullUrl = "http://dateme222.in.ua";
            shortUrl = URLClient.postUrlToRest(fullUrl);
            URLClient.getFullUrl(shortUrl);
            List<URLData.DataStat> stat = URLClient.getStat(shortUrl);
            assertNotNull(stat);
            assertNotSame(stat.size(), 0);
            String country = stat.get(stat.size() - 1).getCountry();
            assertEquals(country.equals(""), false);
        } finally {
            dbHelper.removeShortUrl(shortUrl);
        }
    }

    @Test
    public void testPostNEWUrl() throws Exception {
        String shortUrl = null;
        try {
            String fullUrl = "http://test14444.in.ua";
            shortUrl = URLClient.postUrlToRest(fullUrl);
            assertNotNull(shortUrl);
            URLClient.getFullUrl(shortUrl);
        } finally {
            dbHelper.removeShortUrl(shortUrl);
        }
    }

    @Test
    public void checkCorrectWorkWithLinksFrom1_0() throws Exception {
        String fromServer = URLClient.getFullUrl(SHORT_KEY_1_0_VERSION);
        assertEquals("<html><head><meta HTTP-EQUIV=\"REFRESH\" content=\"0; url=http://marketingbuzz.info/pismo-google-pro-iskusstvennye-vxodyashhie-ssylki-celevaya-rassylka-ili-provokaciya-eksperiment.html\"></head></html>", fromServer);
    }

}
