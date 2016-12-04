package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class RudiSourceTest {

    @Test
    public void testGetParameterList() {
        RudiSource source = new RudiSource(Arrays.asList(
                "subroutine foo(a, b, c)",
                "decs"
        ));
        List<String> list = source.getParameterList();
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("a", list.get(0));
        Assert.assertEquals("b", list.get(1));
        Assert.assertEquals("c", list.get(2));
    }

    @Test
    public void testGetParameterListEmpty() {
        RudiSource source = new RudiSource(Arrays.asList(
                "subroutine foo()"
        ));
        List<String> list = source.getParameterList();
        Assert.assertEquals(0, list.size());
    }
}
