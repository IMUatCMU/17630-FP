package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.error.NotAConstantException;
import rudi.support.literal.Constant;
import rudi.support.literal.ConstantFactory;
import rudi.support.variable.VarType;

@RunWith(JUnit4.class)
public class ConstantFactoryTest {

    @Test
    public void testCreateInteger() {
        Constant c = ConstantFactory.create("10");
        Assert.assertEquals(VarType.INTEGER, c.getType());
        Assert.assertEquals(10, c.getValue());
    }

    @Test
    public void testCreateFloat() {
        Constant c = ConstantFactory.create("10.01");
        Assert.assertEquals(VarType.FLOAT, c.getType());
        Assert.assertEquals(10.01f, c.getValue());
    }

    @Test
    public void testCreateString() {
        Constant c = ConstantFactory.create("\"foo\"");
        Assert.assertEquals(VarType.STRING, c.getType());
        Assert.assertEquals("foo", c.getValue());
    }

    @Test(expected = NotAConstantException.class)
    public void testNotAConstant() {
        ConstantFactory.create("foo");
    }
}
