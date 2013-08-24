package ua.in.link.db;

import org.junit.Before;
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

    @Test
    public void testGenerateNewShort() {
        long start, end;
        start = System.currentTimeMillis();
        DBHelper tester = DBHelper.getInstance();

        // String ans = tester.generateNewShort();
        //System.out.println(ans);

        end=System.currentTimeMillis();
        System.out.println("it's took a "+ (end - start) +" mil");
        assertNotNull(tester);
    }

    @Test
    // Check if first generated URLs are 'a', 'b' and 'c'
    public void testFirstShortURL() {
        DBHelper tester = DBHelper.getInstance();

        String firstShortURLStr = null;
        String secondShortURLStr = null;
        String thirdShortURLStr = null;

        try
        {
            URLData firstShortURL = tester.getShortUrl("FirstLongURL");
            firstShortURLStr = firstShortURL.getShortUrl();

            URLData secondShortURL = tester.getShortUrl("SecondLongURL");
            secondShortURLStr = secondShortURL.getShortUrl();

            URLData thirdShortURL = tester.getShortUrl("ThirdLongURL");
            thirdShortURLStr = thirdShortURL.getShortUrl();

            assertEquals("a", firstShortURL.getShortUrl());
            assertEquals("b", secondShortURL.getShortUrl());
            assertEquals("c", thirdShortURL.getShortUrl());
        } finally {
            tester.removeShortUrl(firstShortURLStr);
            tester.removeShortUrl(secondShortURLStr);
            tester.removeShortUrl(thirdShortURLStr);
        }
    }

}
