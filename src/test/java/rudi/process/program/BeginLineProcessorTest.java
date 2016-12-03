package rudi.process.program;

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
public class BeginLineProcessorTest {

    @Test
    public void testBeginProgram() {
        RudiStack.getInstance().push(RudiContext.defaultContext());
        Assert.assertFalse(RudiStack.currentContext().isExecutionMode());
        BeginLineProcessor.getInstance().doProcess(1, "begin");
        Assert.assertTrue(RudiStack.currentContext().isExecutionMode());
    }

    @Test(expected = CannotProcessLineException.class)
    public void testBeginProgramInDeclarationMode() {
        RudiStack.getInstance().push(RudiContext.defaultContext());
        RudiSource source = new RudiSource(Arrays.asList("begin"));
        RudiStack.currentContext().setSourceCode(source);
        RudiStack.currentContext().setDeclarationMode(true);
        BeginLineProcessor.getInstance().doProcess(1, "begin");
    }

    @Test(expected = CannotProcessLineException.class)
    public void testBeginProgramOnSubLevel() {
        RudiStack.getInstance().push(RudiContext.defaultContext());
        RudiSource source = new RudiSource(Arrays.asList("begin"));
        RudiStack.currentContext().setSourceCode(source);
        RudiStack.currentContext().setBracketDepth(1);
        BeginLineProcessor.getInstance().doProcess(1, "begin");
    }
}
