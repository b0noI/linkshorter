package ua.in.link.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.in.link.rest.BaseRestTest;
import ua.in.link.rest.client.URLClient;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

/**
 * User: b0noI
 * Date: 01.05.13
 * Time: 23:14
 */
public class LimitsTest extends BaseRestTest {

    DBHelper dbHelper;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        dbHelper = DBHelper.getInstance();
        dbHelper.setIgnoreLocalHost(true);
    }

    @Test
    public void testLimitsUpload() throws Exception{
        ArrayList<String> resultUrls = new ArrayList<>();

        try {
            for (int i = 0; i < 200; i++) {
                String fullUrl = "http://test245" + i + ".in.ua";
                String shortULR = URLClient.postUrlToRest(fullUrl);
                resultUrls.add(shortULR);
            }
        } catch (RuntimeException e) {
            assertNotNull(e);
            return;
        } finally {
            for (String shortURL : resultUrls) {
                dbHelper.removeShortUrl(shortURL);
            }
        }

        assertNotNull(null);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        DBHelper.getInstance().setIgnoreLocalHost(false);
    }

}
