package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RudiUtilsTest {

    @Test
    public void testStripComments() {
        Assert.assertEquals("something ", RudiUtils.stripComments("something /* comment */"));
    }
}
