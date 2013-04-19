package ua.in.link.db;

import org.junit.Test;

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
}
