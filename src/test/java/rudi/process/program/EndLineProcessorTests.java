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
public class EndLineProcessorTests {

    @Test
    public void testEndLineProcessor() {
        RudiStack.getInstance().removeAllElements();
        RudiStack.getInstance().push(RudiContext.defaultContext());
        RudiStack.currentContext().setExecutionMode(true);

        EndLineProcessor.getInstance().doProcess(1, "end");
        Assert.assertEquals(0, RudiStack.getInstance().size());
    }

    @Test(expected = CannotProcessLineException.class)
    public void testEndLineProcessorOutOfExecutionMode() {
        RudiStack.getInstance().push(RudiContext.defaultContext());
        RudiStack.currentContext().setExecutionMode(false);
        RudiStack.currentContext().setSourceCode(new RudiSource(Arrays.asList("end")));
        EndLineProcessor.getInstance().doProcess(1, "end");
    }
}
