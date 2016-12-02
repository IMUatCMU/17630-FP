package rudi.process.program;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.process.LineProcessor;
import rudi.support.RudiSource;
import rudi.support.RudiStack;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class ProgramLineProcessorTests {

    @Before
    public void setup() {
        RudiStack.getInstance().removeAllElements();
    }

    @Test
    public void testProgram() {
        RudiSource source = new RudiSource(Arrays.asList(
                "program"
        ));

        Assert.assertEquals(0, RudiStack.getInstance().size());
        Assert.assertTrue(ProgramLineProcessor.getInstance().canProcess(source.getLine(1)));
        ProgramLineProcessor.getInstance().doProcess(1, source.getLine(1));
        Assert.assertEquals(1, RudiStack.getInstance().size());
    }
}
