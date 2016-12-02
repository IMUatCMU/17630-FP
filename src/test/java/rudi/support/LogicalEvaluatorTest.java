package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.support.expression.token.ConstantToken;
import rudi.support.expression.token.LogicalOperatorToken;
import rudi.support.expression.token.Token;
import rudi.support.expression.token.TokenFactory;
import rudi.support.literal.Constant;
import rudi.support.variable.VarType;

@RunWith(JUnit4.class)
public class LogicalEvaluatorTest {

    @Test
    public void testAnd() {
        testBody("^", true, true, true);
        testBody("^", true, false, false);
        testBody("^", false, true, false);
        testBody("^", false, false, false);
    }

    @Test
    public void testOr() {
        testBody("|", true, true, true);
        testBody("|", true, false, true);
        testBody("|", false, true, true);
        testBody("|", false, false, false);
    }

    @Test
    public void testNot() {
        testBody("~", true, true, false);
        testBody("~", true, false, false);
        testBody("~", false, true, true);
        testBody("~", false, false, true);
    }

    private void testBody(String op, Boolean lhs, Boolean rhs, Boolean expectedResult) {
        LogicalOperatorToken tok = (LogicalOperatorToken) TokenFactory.create(op);
        ConstantToken c1 = new ConstantToken(lhs.toString(), new Constant(VarType.BOOLEAN, lhs));
        ConstantToken c2 = new ConstantToken(rhs.toString(), new Constant(VarType.BOOLEAN, rhs));
        Token r = tok.evaluator().evaluate(c1, c2);

        Assert.assertEquals(ConstantToken.class, r.getClass());
        Assert.assertEquals(VarType.BOOLEAN, ((ConstantToken) r).getConstant().getType());
        Assert.assertEquals(expectedResult, ((ConstantToken) r).getConstant().getValue());
    }
}
