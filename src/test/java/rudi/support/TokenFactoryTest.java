package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.error.UnrecognizedTokenException;
import rudi.support.expression.token.*;
import rudi.support.variable.VarType;
import rudi.support.variable.Variable;

@RunWith(JUnit4.class)
public class TokenFactoryTest {

    @Test
    public void testCreateArithmeticAdd() {
        Token tok = TokenFactory.create("+");
        Assert.assertEquals(ArithmeticOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.ADD, tok.getFaceValue());
    }

    @Test
    public void testCreateArithmeticSubtract() {
        Token tok = TokenFactory.create("-");
        Assert.assertEquals(ArithmeticOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.MINUS, tok.getFaceValue());
    }

    @Test
    public void testCreateArithmeticMultiply() {
        Token tok = TokenFactory.create("*");
        Assert.assertEquals(ArithmeticOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.MULTIPLY, tok.getFaceValue());
    }

    @Test
    public void testCreateArithmeticDivision() {
        Token tok = TokenFactory.create("/");
        Assert.assertEquals(ArithmeticOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.DIVIDE, tok.getFaceValue());
    }

    @Test
    public void testCreateLogicalAnd() {
        Token tok = TokenFactory.create("^");
        Assert.assertEquals(LogicalOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.AND, tok.getFaceValue());
    }

    @Test
    public void testCreateLogicalOr() {
        Token tok = TokenFactory.create("|");
        Assert.assertEquals(LogicalOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.OR, tok.getFaceValue());
    }

    @Test
    public void testCreateLogicalNot() {
        Token tok = TokenFactory.create("~");
        Assert.assertEquals(LogicalOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.NOT, tok.getFaceValue());
    }

    @Test
    public void testCreateRelationalEq() {
        Token tok = TokenFactory.create(":eq:");
        Assert.assertEquals(RelationalOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.EQ, tok.getFaceValue());
    }

    @Test
    public void testCreateRelationalNe() {
        Token tok = TokenFactory.create(":ne:");
        Assert.assertEquals(RelationalOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.NE, tok.getFaceValue());
    }

    @Test
    public void testCreateRelationalGt() {
        Token tok = TokenFactory.create(":gt:");
        Assert.assertEquals(RelationalOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.GT, tok.getFaceValue());
    }

    @Test
    public void testCreateRelationalGe() {
        Token tok = TokenFactory.create(":ge:");
        Assert.assertEquals(RelationalOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.GE, tok.getFaceValue());
    }

    @Test
    public void testCreateRelationalLt() {
        Token tok = TokenFactory.create(":lt:");
        Assert.assertEquals(RelationalOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.LT, tok.getFaceValue());
    }

    @Test
    public void testCreateRelationalLe() {
        Token tok = TokenFactory.create(":le:");
        Assert.assertEquals(RelationalOperatorToken.class, tok.getClass());
        Assert.assertEquals(RudiConstant.LE, tok.getFaceValue());
    }

    @Test
    public void testCreateIntegerConstant() {
        Token tok = TokenFactory.create("10");
        Assert.assertEquals(ConstantToken.class, tok.getClass());
        Assert.assertEquals(VarType.INTEGER, ((ConstantToken) tok).getConstant().getType());
        Assert.assertEquals(10, ((ConstantToken) tok).getConstant().getValue());
    }

    @Test
    public void testCreateFloatConstant() {
        Token tok = TokenFactory.create("123.321");
        Assert.assertEquals(ConstantToken.class, tok.getClass());
        Assert.assertEquals(VarType.FLOAT, ((ConstantToken) tok).getConstant().getType());
        Assert.assertEquals(123.321f, ((ConstantToken) tok).getConstant().getValue());
    }

    @Test
    public void testCreateStringConstant() {
        Token tok = TokenFactory.create("\"foo\"");
        Assert.assertEquals(ConstantToken.class, tok.getClass());
        Assert.assertEquals(VarType.STRING, ((ConstantToken) tok).getConstant().getType());
        Assert.assertEquals("foo", ((ConstantToken) tok).getConstant().getValue());
    }

    @Test
    public void testCreateVariable() {
        RudiContext ctx = RudiContext.defaultContext();
        ctx.declare(new Variable(VarType.STRING, "foo"));
        ctx.modifier("foo").modify("bar");
        RudiStack.getInstance().push(ctx);

        Token tok = TokenFactory.create("foo");
        Assert.assertEquals(VariableToken.class, tok.getClass());
        Assert.assertEquals("bar", ((VariableToken) tok).getAccessor().access().getValue());
    }

    @Test(expected = UnrecognizedTokenException.class)
    public void testCreateTokenFail() {
        RudiStack.getInstance().push(RudiContext.defaultContext());
        TokenFactory.create("foo");
    }
}
