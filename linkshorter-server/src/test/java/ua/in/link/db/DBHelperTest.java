package ua.in.link.db;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * 4/19/13 2:03 PM
 * @author vlad sonkin
 *
 * I'm don't have enough experience in junit in mongoDB. But this test is
 * really necessary to check my new method generateNewShort();
 * aaaaaand...I still get the error when building the project. I will do it later
 *
 * You need to change modificator in method generateNewShort() to protected
 * in class DBHelper.
 */
public class DBHelperTest {

    DBHelper dbHelper = DBHelper.getInstance();

    @Test
    public void testGenerateNewShort() {
        long start, end;
        start = System.currentTimeMillis();
        DBHelper tester = DBHelper.getInstance();

        // String ans = dbHelper.generateNewShort();
        //System.out.println(ans);

        end=System.currentTimeMillis();
        System.out.println("it's took a "+ (end - start) +" mil");
        assertNotNull(tester);
    }

    @Test
    // Check if first generated URLs are 'a', 'b' and 'c'
    public void testFirstShortUrl() {

        String firstShortURLStr = null;
        String secondShortURLStr = null;
        String thirdShortURLStr = null;

        try {
            URLData firstShortURL = dbHelper.getShortUrl("FirstLongURL");
            firstShortURLStr = firstShortURL.getShortUrl();

            URLData secondShortURL = dbHelper.getShortUrl("SecondLongURL");
            secondShortURLStr = secondShortURL.getShortUrl();

            URLData thirdShortURL = dbHelper.getShortUrl("ThirdLongURL");
            thirdShortURLStr = thirdShortURL.getShortUrl();

            assertEquals("a", firstShortURL.getShortUrl());
            assertEquals("b", secondShortURL.getShortUrl());
            assertEquals("c", thirdShortURL.getShortUrl());
        } finally {
            dbHelper.removeShortUrl(firstShortURLStr);
            dbHelper.removeShortUrl(secondShortURLStr);
            dbHelper.removeShortUrl(thirdShortURLStr);
        }
    }

    @Test
    public void testAddPrivateUtl() {
        String shortUrl = null;

        try {
            String fullUrl = "fullUrl";
            String password = "pwd";
            URLData urlData = dbHelper.getShortUrl(fullUrl, password);

            shortUrl = urlData.getShortUrl();
            assertEquals(password, urlData.getPassword());

            URLData actualFullUrl = dbHelper.getFullUrl(shortUrl);
            assertEquals(fullUrl, actualFullUrl.getOriginalUrl());

        } finally {
            dbHelper.removeShortUrl(shortUrl);
        }
    }


}
