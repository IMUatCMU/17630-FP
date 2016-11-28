package rudi.process.print;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.process.LineProcessor;
import rudi.support.RudiContext;
import rudi.support.RudiSource;
import rudi.support.RudiStack;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class PrintLineProcessorTests {

    private ByteArrayOutputStream myOut;

    @Before
    public void setup() {
        myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
    }

    @Test
    public void testPrint() {
        RudiSource source = new RudiSource(Arrays.asList(
                "print \"Hello World\""
        ));
        RudiContext context = RudiContext.defaultContext();
        context.setSourceCode(source);
        RudiStack.getInstance().push(context);

        Assert.assertTrue(PrintLineProcessor.getInstance().canProcess(source.getLine(1)));
        PrintLineProcessor.getInstance().doProcess(1, source.getLine(1));
        Assert.assertEquals("Hello World", myOut.toString());
    }
}
