package rudi.process.input;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.error.CannotProcessLineException;
import rudi.support.RudiContext;
import rudi.support.RudiSource;
import rudi.support.RudiStack;
import rudi.support.variable.VarType;
import rudi.support.variable.Variable;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class InputLineProcessorTests {

    @Test
    public void testStringInput() {
        ByteArrayInputStream myIn = new ByteArrayInputStream("My string".getBytes());
        System.setIn(myIn);
        RudiSource source = new RudiSource(Arrays.asList(
                "input myString"
        ));
        RudiContext context = RudiContext.defaultContext();
        context.declare(new Variable(VarType.STRING, "myString"));
        context.setSourceCode(source);
        RudiStack.getInstance().push(context);

        Assert.assertTrue(InputLineProcessor.getInstance().canProcess(source.getLine(1)));
        InputLineProcessor.getInstance().doProcess(1, source.getLine(1));

        Assert.assertEquals("My string", context.accessor("myString").access().getValue());
    }

    @Test
    public void testIntInput() {
        ByteArrayInputStream myIn = new ByteArrayInputStream("123".getBytes());
        System.setIn(myIn);
        RudiSource source = new RudiSource(Arrays.asList(
                "input myInt"
        ));
        RudiContext context = RudiContext.defaultContext();
        context.declare(new Variable(VarType.INTEGER, "myInt"));
        context.setSourceCode(source);
        RudiStack.getInstance().push(context);

        Assert.assertTrue(InputLineProcessor.getInstance().canProcess(source.getLine(1)));
        InputLineProcessor.getInstance().doProcess(1, source.getLine(1));

        Assert.assertEquals(123, context.accessor("myInt").access().getValue());
    }

    @Test
    public void testFloatInputToIntVariable() {
        ByteArrayInputStream myIn = new ByteArrayInputStream("100.99".getBytes());
        System.setIn(myIn);
        RudiSource source = new RudiSource(Arrays.asList(
                "input myInt"
        ));
        RudiContext context = RudiContext.defaultContext();
        context.declare(new Variable(VarType.INTEGER, "myInt"));
        context.setSourceCode(source);
        RudiStack.getInstance().push(context);

        Assert.assertTrue(InputLineProcessor.getInstance().canProcess(source.getLine(1)));
        InputLineProcessor.getInstance().doProcess(1, source.getLine(1));

        Assert.assertEquals(101, context.accessor("myInt").access().getValue());
    }

    @Test(expected = CannotProcessLineException.class)
    public void testStringInputToIntVariable() {
        ByteArrayInputStream myIn = new ByteArrayInputStream("Michael".getBytes());
        System.setIn(myIn);
        RudiSource source = new RudiSource(Arrays.asList(
                "input amount"
        ));
        RudiContext context = RudiContext.defaultContext();
        context.declare(new Variable(VarType.INTEGER, "amount"));
        context.setSourceCode(source);
        RudiStack.getInstance().push(context);

        Assert.assertTrue(InputLineProcessor.getInstance().canProcess(source.getLine(1)));
        InputLineProcessor.getInstance().doProcess(1, source.getLine(1));
    }
}
