package rudi.process.decs;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.error.CannotProcessLineException;
import rudi.support.RudiContext;
import rudi.support.RudiSource;
import rudi.support.RudiStack;
import rudi.support.variable.VarType;
import rudi.support.variable.VariableAccessor;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class VariableDeclarationProcessorTests {

    @Test
    public void testDeclareInteger() {
        RudiContext ctx = RudiContext.defaultContext();
        ctx.setDeclarationMode(true);
        RudiStack.getInstance().push(ctx);

        String code = "integer fact /* a comment */";

        Assert.assertTrue(VariableDeclarationProcessor.getInstance().canProcess(code));
        VariableDeclarationProcessor.getInstance().doProcess(1, code);
        VariableAccessor va = RudiStack.getInstance().peek().accessor("fact");
        Assert.assertNotNull(va);
        Assert.assertEquals("fact", va.access().getName());
        Assert.assertEquals(VarType.INTEGER, va.access().getType());
        Assert.assertNull(va.access().getValue());
    }

    @Test
    public void testDeclareFloat() {
        RudiContext ctx = RudiContext.defaultContext();
        ctx.setDeclarationMode(true);
        RudiStack.getInstance().push(ctx);

        String code = "float  fact";

        Assert.assertTrue(VariableDeclarationProcessor.getInstance().canProcess(code));
        VariableDeclarationProcessor.getInstance().doProcess(1, code);
        VariableAccessor va = RudiStack.getInstance().peek().accessor("fact");
        Assert.assertNotNull(va);
        Assert.assertEquals("fact", va.access().getName());
        Assert.assertEquals(VarType.FLOAT, va.access().getType());
        Assert.assertNull(va.access().getValue());
    }

    @Test
    public void testDeclareString() {
        RudiContext ctx = RudiContext.defaultContext();
        ctx.setDeclarationMode(true);
        RudiStack.getInstance().push(ctx);

        String code = "string fact ";

        Assert.assertTrue(VariableDeclarationProcessor.getInstance().canProcess(code));
        VariableDeclarationProcessor.getInstance().doProcess(1, code);
        VariableAccessor va = RudiStack.getInstance().peek().accessor("fact");
        Assert.assertNotNull(va);
        Assert.assertEquals("fact", va.access().getName());
        Assert.assertEquals(VarType.STRING, va.access().getType());
        Assert.assertNull(va.access().getValue());
    }

    @Test(expected = CannotProcessLineException.class)
    public void testDeclareNotInDeclarationMode() {
        String code = "string fact";

        RudiContext ctx = RudiContext.defaultContext();
        ctx.setSourceCode(new RudiSource(Arrays.asList(code)));
        ctx.setDeclarationMode(false);
        RudiStack.getInstance().push(ctx);

        VariableDeclarationProcessor.getInstance().doProcess(1, code);
    }
}
