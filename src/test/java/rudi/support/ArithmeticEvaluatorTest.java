package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.support.expression.token.ConstantToken;
import rudi.support.expression.token.Token;
import rudi.support.expression.token.TokenFactory;
import rudi.support.variable.VarType;

@RunWith(JUnit4.class)
public class ArithmeticEvaluatorTest {

    @Test
    public void testAddIntToInt() {
        testBody("+", "1", "2", VarType.INTEGER, 3);
    }

    @Test
    public void testSubtractIntFromInt() {
        testBody("-", "2", "1", VarType.INTEGER, 1);
    }

    @Test
    public void testMultiplyIntToInt() {
        testBody("*", "3", "7", VarType.INTEGER, 21);
    }

    @Test
    public void testDivideIntFromInt() {
        testBody("/", "7", "3", VarType.INTEGER, 2);
    }

    @Test
    public void testAddFloatToInt() {
        testBody("+", "1", "2.1", VarType.FLOAT, 3.1f);
    }

    private void testBody(String op, String lhs, String rhs, VarType expectedType, Object expectedValue) {
        Token tok = TokenFactory.create(op).evaluator().evaluate(
                TokenFactory.create(lhs),
                TokenFactory.create(rhs)
        );
        Assert.assertNotNull(tok);
        Assert.assertEquals(ConstantToken.class, tok.getClass());
        Assert.assertEquals(expectedType, ((ConstantToken) tok).getConstant().getType());
        Assert.assertEquals(expectedValue, ((ConstantToken) tok).getConstant().getValue());
    }
}
