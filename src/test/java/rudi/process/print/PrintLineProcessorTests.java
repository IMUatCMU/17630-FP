package rudi.process.print;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.support.RudiContext;
import rudi.support.RudiSource;
import rudi.support.RudiStack;
import rudi.support.variable.VarType;
import rudi.support.variable.Variable;

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

    @Test
    public void testPrintVariable() {
        RudiSource source = new RudiSource(Arrays.asList(
                "print foo"
        ));
        RudiContext context = RudiContext.defaultContext();
        context.setSourceCode(source);
        context.declare(new Variable(VarType.STRING, "foo"));
        context.modifier("foo").modify("bar");
        RudiStack.getInstance().push(context);

        Assert.assertTrue(PrintLineProcessor.getInstance().canProcess(source.getLine(1)));
        PrintLineProcessor.getInstance().doProcess(1, source.getLine(1));
        Assert.assertEquals("bar", myOut.toString());
    }

    @Test
    public void testPrintNewLine() {
        RudiSource source = new RudiSource(Arrays.asList(
                "print cr"
        ));
        RudiContext context = RudiContext.defaultContext();
        context.setSourceCode(source);
        RudiStack.getInstance().push(context);

        Assert.assertTrue(PrintLineProcessor.getInstance().canProcess(source.getLine(1)));
        PrintLineProcessor.getInstance().doProcess(1, source.getLine(1));
        Assert.assertEquals("\n", myOut.toString());
    }
}
