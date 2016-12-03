package rudi.process.pre;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.error.CannotProcessLineException;
import rudi.support.RudiConstant;
import rudi.support.RudiSource;
import rudi.support.RudiSourceRegistry;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class SourcePreProcessorTest {

    @Test
    public void testParseMain() {
        RudiSource source = new RudiSource(Arrays.asList(
                "/* An inline comment by its self */",
                "/* A",
                "multi-line",
                "comment */",
                "program",
                "decs",
                "[",
                "   integer n   /* an inline comment */",
                "]",
                "begin",
                "   n = 3",
                "   print n",
                "end"
        ));
        source = SourcePreProcessor.process(source);

        Assert.assertEquals(13, source.totalLines());

        RudiSource main = RudiSourceRegistry.getInstance().get(RudiConstant.MAIN_PROGRAM_KEY);
        Assert.assertNotNull(main);
        Assert.assertEquals(4, main.getGlobalLineOffset());
        Assert.assertEquals(9, main.totalLines());
        Assert.assertEquals("program", main.getLine(1));
        Assert.assertEquals("decs", main.getLine(2));
        Assert.assertEquals("[", main.getLine(3));
        Assert.assertEquals("integer n", main.getLine(4));
        Assert.assertEquals("]", main.getLine(5));
        Assert.assertEquals("begin", main.getLine(6));
        Assert.assertEquals("n = 3", main.getLine(7));
        Assert.assertEquals("print n", main.getLine(8));
        Assert.assertEquals("end", main.getLine(9));
    }

    @Test
    public void testLineContinuation() {
        RudiSource source = new RudiSource(Arrays.asList(
                "program",
                "decs",
                "[",
                "   integer n   /* an inline comment */",
                "]",
                "begin",
                "   n = ( 3 + &",
                "       4 ) * 5",
                "   print n",
                "end"
        ));
        SourcePreProcessor.process(source);

        RudiSource main = RudiSourceRegistry.getInstance().get(RudiConstant.MAIN_PROGRAM_KEY);
        Assert.assertNotNull(main);
        Assert.assertEquals(0, main.getGlobalLineOffset());
        Assert.assertEquals(10, main.totalLines());
        Assert.assertEquals("program", main.getLine(1));
        Assert.assertEquals("decs", main.getLine(2));
        Assert.assertEquals("[", main.getLine(3));
        Assert.assertEquals("integer n", main.getLine(4));
        Assert.assertEquals("]", main.getLine(5));
        Assert.assertEquals("begin", main.getLine(6));
        Assert.assertEquals("n = ( 3 + 4 ) * 5", main.getLine(7));
        Assert.assertEquals("", main.getLine(8));
        Assert.assertEquals("print n", main.getLine(9));
        Assert.assertEquals("end", main.getLine(10));
    }

    @Test
    public void testParseMainWithSubroutine() {
        RudiSource source = new RudiSource(Arrays.asList(
                "program",
                "decs",
                "[",
                "   integer n   /* an inline comment */",
                "]",
                "begin",
                "   n = 3",
                "   foo(n, n)",
                "end",
                "",
                "subroutine foo(n, m)",
                "begin",
                "   input n",
                "   print m",
                "return"
        ));
        source = SourcePreProcessor.process(source);
        Assert.assertEquals(15, source.totalLines());

        RudiSource main = RudiSourceRegistry.getInstance().get(RudiConstant.MAIN_PROGRAM_KEY);
        Assert.assertNotNull(main);
        Assert.assertEquals(0, main.getGlobalLineOffset());
        Assert.assertEquals(9, main.totalLines());
        Assert.assertEquals("program", main.getLine(1));
        Assert.assertEquals("decs", main.getLine(2));
        Assert.assertEquals("[", main.getLine(3));
        Assert.assertEquals("integer n", main.getLine(4));
        Assert.assertEquals("]", main.getLine(5));
        Assert.assertEquals("begin", main.getLine(6));
        Assert.assertEquals("n = 3", main.getLine(7));
        Assert.assertEquals("foo(n, n)", main.getLine(8));
        Assert.assertEquals("end", main.getLine(9));

        RudiSource foo = RudiSourceRegistry.getInstance().get("foo");
        Assert.assertNotNull(foo);
        Assert.assertEquals(10, foo.getGlobalLineOffset());
        Assert.assertEquals(5, foo.totalLines());
        Assert.assertEquals("subroutine foo(n, m)", foo.getLine(1));
        Assert.assertEquals("begin", foo.getLine(2));
        Assert.assertEquals("input n", foo.getLine(3));
        Assert.assertEquals("print m", foo.getLine(4));
        Assert.assertEquals("return", foo.getLine(5));
    }

    @Test(expected = CannotProcessLineException.class)
    public void testIllegalBracketPlacement() {
        RudiSource source = new RudiSource(Arrays.asList(
                "program",
                "decs [ /* this is wrong */",
                "   integer n",
                "]",
                "begin",
                "   n = 3",
                "   foo(n, n)",
                "end"
        ));
        SourcePreProcessor.process(source);
    }

    @Test(expected = CannotProcessLineException.class)
    public void testMissingProgramEnd() {
        RudiSource source = new RudiSource(Arrays.asList(
                "program",
                "decs",
                "[",
                "   integer n",
                "]",
                "begin",
                "   n = 3",
                "   foo(n, n)"
        ));
        SourcePreProcessor.process(source);
    }
}
