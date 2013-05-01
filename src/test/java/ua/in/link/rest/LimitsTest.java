package ua.in.link.rest;

import org.junit.Test;
import ua.in.link.rest.client.URLClient;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: b0noI
 * Date: 01.05.13
 * Time: 23:14
 * To change this template use File | Settings | File Templates.
 */
public class LimitsTest {

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

}
