package rudi.process.decs;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.error.CannotProcessLineException;
import rudi.support.RudiContext;
import rudi.support.RudiStack;

@RunWith(JUnit4.class)
public class DecsLineProcessorTest {

    @Test
    public void testDecs() {
        RudiContext ctx = RudiContext.defaultContext();
        RudiStack.getInstance().push(ctx);

        Assert.assertFalse(RudiStack.getInstance().peek().isDeclarationMode());
        Assert.assertTrue(DecsLineProcessor.getInstace().canProcess("decs"));
        DecsLineProcessor.getInstace().doProcess(1, "decs");
        Assert.assertTrue(RudiStack.getInstance().peek().isDeclarationMode());
    }

    @Test
    public void testDecsWithComments() {
        String code = "decs /* this is a comment */";

        RudiContext ctx = RudiContext.defaultContext();
        RudiStack.getInstance().push(ctx);

        Assert.assertFalse(RudiStack.getInstance().peek().isDeclarationMode());
        Assert.assertTrue(DecsLineProcessor.getInstace().canProcess(code));
        DecsLineProcessor.getInstace().doProcess(1, code);
        Assert.assertTrue(RudiStack.getInstance().peek().isDeclarationMode());
    }

    @Test(expected = CannotProcessLineException.class)
    public void testDecsWithBracketOnSameLevel() {
        String code = "   decs [ /* this is a comment */";

        RudiContext ctx = RudiContext.defaultContext();
        RudiStack.getInstance().push(ctx);

        Assert.assertFalse(RudiStack.getInstance().peek().isDeclarationMode());
        Assert.assertTrue(DecsLineProcessor.getInstace().canProcess(code));
        DecsLineProcessor.getInstace().doProcess(1, code);
    }
}
