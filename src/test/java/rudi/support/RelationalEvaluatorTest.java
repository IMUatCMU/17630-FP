package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.support.expression.token.ConstantToken;
import rudi.support.expression.token.TokenFactory;
import rudi.support.variable.VarType;

@RunWith(JUnit4.class)
public class RelationalEvaluatorTest {

    @Test
    public void testEqual() {
        testBody(":eq:", "1", "1", true);
        testBody(":eq:", "1", "2", false);
        testBody(":eq:", "1.1", "1.1", true);
        testBody(":eq:", "1.1", "2.1", false);
        testBody(":eq:", "1", "1.0", true);
        testBody(":eq:", "\"foo\"", "\"foo\"", true);
        testBody(":eq:", "\"foo\"", "\"bar\"", false);
    }

    @Test
    public void testNotEqual() {
        testBody(":ne:", "1", "1", false);
        testBody(":ne:", "1", "2", true);
        testBody(":ne:", "1.1", "1.1", false);
        testBody(":ne:", "1.1", "2.1", true);
        testBody(":ne:", "1", "1.0", false);
        testBody(":ne:", "\"foo\"", "\"foo\"", false);
        testBody(":ne:", "\"foo\"", "\"bar\"", true);
    }

    @Test
    public void testGreaterThan() {
        testBody(":gt:", "1", "1", false);
        testBody(":gt:", "2", "1", true);
    }

    @Test
    public void testGreaterEqual() {
        testBody(":ge:", "1", "1", true);
        testBody(":ge:", "2", "1", true);
    }

    @Test
    public void testLessThan() {
        testBody(":lt:", "1", "1", false);
        testBody(":lt:", "1", "2", true);
    }

    @Test
    public void testLessThanEqual() {
        testBody(":le:", "1", "1", true);
        testBody(":le:", "0", "1", true);
    }

    private void testBody(String op, String lhs, String rhs, Boolean expectedResult) {
        ConstantToken c = (ConstantToken) TokenFactory.create(op)
                .evaluator()
                .evaluate(
                        TokenFactory.create(lhs),
                        TokenFactory.create(rhs)
                );

        Assert.assertEquals(VarType.BOOLEAN, c.getConstant().getType());
        Assert.assertEquals(expectedResult, c.getConstant().getValue());
    }
}
