package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.support.expression.eval.ExpressionResolver;
import rudi.support.literal.Constant;
import rudi.support.variable.VarType;
import rudi.support.variable.Variable;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class ExpressionResolverTest {

    @Test
    public void testSimpleArithmetic() {
        Constant c = ExpressionResolver.resolve(tok("1", "+", "2"));
        Assert.assertEquals(VarType.INTEGER, c.getType());
        Assert.assertEquals(3, c.getValue());

        c = ExpressionResolver.resolve(tok("18", "-", "14"));
        Assert.assertEquals(VarType.INTEGER, c.getType());
        Assert.assertEquals(4, c.getValue());

        c = ExpressionResolver.resolve(tok("5", "*", "4"));
        Assert.assertEquals(VarType.INTEGER, c.getType());
        Assert.assertEquals(20, c.getValue());

        c = ExpressionResolver.resolve(tok("20", "/", "2"));
        Assert.assertEquals(VarType.INTEGER, c.getType());
        Assert.assertEquals(10, c.getValue());
    }

    @Test
    public void testCompoundArithmetic() {
        Constant c = ExpressionResolver.resolve(tok("1", "+", "2", "*", "4"));
        Assert.assertEquals(VarType.INTEGER, c.getType());
        Assert.assertEquals(9, c.getValue());

        c = ExpressionResolver.resolve(tok("1", "+", "2", "*", "4"));
        Assert.assertEquals(VarType.INTEGER, c.getType());
        Assert.assertEquals(9, c.getValue());

        c = ExpressionResolver.resolve(tok("10", "/", "2", "*", "4"));
        Assert.assertEquals(VarType.INTEGER, c.getType());
        Assert.assertEquals(20, c.getValue());

        c = ExpressionResolver.resolve(tok("(", "1", "+", "2", ")", "*", "4", "-", "24", "/", "2"));
        Assert.assertEquals(VarType.INTEGER, c.getType());
        Assert.assertEquals(0, c.getValue());
    }

    @Test
    public void testRelationalAndLogical() {
        Constant c = ExpressionResolver.resolve(tok("1", "+", "2", ":eq:", "3"));
        Assert.assertEquals(VarType.BOOLEAN, c.getType());
        Assert.assertEquals(true, c.getValue());

        c = ExpressionResolver.resolve(tok("1", "+", "2", ":gt:", "4"));
        Assert.assertEquals(VarType.BOOLEAN, c.getType());
        Assert.assertEquals(false, c.getValue());

        c = ExpressionResolver.resolve(tok("1", "+", "2", ":gt:", "4", "|", "3", ":lt:", "4"));
        Assert.assertEquals(VarType.BOOLEAN, c.getType());
        Assert.assertEquals(true, c.getValue());
    }

    @Test
    public void testRealisticExpression() {
        RudiContext ctx = RudiContext.defaultContext();
        ctx.declare(new Variable(VarType.STRING, "foo"));
        ctx.modifier("foo").modify("bar");
        RudiStack.getInstance().push(ctx);

        Constant c = ExpressionResolver.resolve(tok("foo", ":eq:", "\"bar\"", "^", "~", "4.1", ":gt:", "5.1"));
        Assert.assertEquals(VarType.BOOLEAN, c.getType());
        Assert.assertEquals(true, c.getValue());
    }

    private static List<String> tok(String... raw) {
        return Arrays.asList(raw);
    }
}
