package rudi.process.decs;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.error.CannotProcessLineException;
import rudi.support.RudiContext;
import rudi.support.RudiSource;
import rudi.support.RudiStack;

import java.util.Arrays;

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
        RudiSource source = new RudiSource(Arrays.asList(code));

        RudiContext ctx = RudiContext.defaultContext();
        ctx.setSourceCode(source);
        RudiStack.getInstance().push(ctx);

        Assert.assertFalse(RudiStack.getInstance().peek().isDeclarationMode());
        Assert.assertTrue(DecsLineProcessor.getInstace().canProcess(code));
        DecsLineProcessor.getInstace().doProcess(1, code);
    }
}
