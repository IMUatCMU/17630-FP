package rudi.process.var;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.support.RudiContext;
import rudi.support.RudiStack;
import rudi.support.variable.VarType;
import rudi.support.variable.Variable;

@RunWith(JUnit4.class)
public class VariableAssignmentProcessorTest {

    @Test
    public void testAssigningSimpleValue() {
        testBody("x = 5", 5);
    }

    @Test
    public void testAssigningExpression() {
        testBody("x = ( 5 + 4 ) * 20 / 10 - 2", 16);
    }

    @Test
    public void testAssigningExpressionWithOtherVariable() {
        RudiContext ctx = RudiContext.defaultContext();
        ctx.setExecutionMode(true);
        ctx.declare(new Variable(VarType.INTEGER, "x"));
        ctx.declare(new Variable(VarType.INTEGER, "y"));
        RudiStack.getInstance().push(ctx);

        VariableAssignmentProcessor.getInstance().doProcess(1, "x = 5");
        VariableAssignmentProcessor.getInstance().doProcess(1, "y = 5 * x + 4");
        Assert.assertEquals(29, RudiStack.currentContext().accessor("y").access().getValue());
    }

    private void testBody(String code, Integer expectedResult) {
        RudiContext ctx = RudiContext.defaultContext();
        ctx.setExecutionMode(true);
        ctx.declare(new Variable(VarType.INTEGER, "x"));
        RudiStack.getInstance().push(ctx);

        Assert.assertTrue(VariableAssignmentProcessor.getInstance().canProcess(code));
        VariableAssignmentProcessor.getInstance().doProcess(1, code);
        Assert.assertEquals(expectedResult, RudiStack.currentContext().accessor("x").access().getValue());
    }
}
