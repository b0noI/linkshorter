package ua.in.link.utils;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * 4/18/13 12:36 PM
 *
 * @author vlad sonkin
 */
public class RandomStringTest {
    @Test
    public void testNextString() throws Exception {
        long start, end;
        start = System.currentTimeMillis();
        RandomString randomString = new RandomString(5000);
        String string = randomString.nextString();
        end=System.currentTimeMillis();
        System.out.println(string +" it's took a "+ (end - start) +" mil");
        assertNotNull(string);
    }
}
