package ua.in.link.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.in.link.rest.BaseRestTest;
import ua.in.link.rest.client.URLClient;

import static org.junit.Assert.assertNotNull;

/**
 * User: b0noI
 * Date: 01.05.13
 * Time: 23:14
 */
public class LimitsTest extends BaseRestTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        DBHelper.getInstance().setIgnoreLocalHost(true);
    }

    @Test
    public void testLimitsUpload() throws Exception{
        try {
            for (int i = 0; i < 200; i++) {
                String fullUrl = "http://test245" + i + ".in.ua";
                URLClient.postUrlToRest(fullUrl);
            }
        } catch (RuntimeException e) {
            assertNotNull(e);
            return;
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
