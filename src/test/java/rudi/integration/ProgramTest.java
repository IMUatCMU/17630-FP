package rudi.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.process.DefaultLineProcessor;
import rudi.process.DelegatingLineProcessor;
import rudi.process.LineProcessor;
import rudi.process.comment.EndCommentModeLineProcessor;
import rudi.process.comment.IgnoreCommentLineProcessor;
import rudi.process.comment.StartCommentModeLineProcessor;
import rudi.process.decs.DecsLineProcessor;
import rudi.process.decs.VariableDeclarationProcessor;
import rudi.process.pre.SourcePreProcessor;
import rudi.process.print.PrintLineProcessor;
import rudi.process.program.*;
import rudi.process.var.VariableAssignmentProcessor;
import rudi.support.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class ProgramTest {

    private ByteArrayOutputStream myOut;

    @Before
    public void setup() {
        myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
    }

    @Test
    public void testWithoutControlStructures() {
        RudiSource source = new RudiSource(Arrays.asList(
                "program",
                "decs",
                "[",
                "   integer x /* an inline comment */",
                "   float y",
                "   string z",
                "]",
                "begin",
                "   x = 2",
                "   y = 1.1 + x",
                "   z = \"hello\"",
                "   print \"x is \"",
                "   print x",
                "   print cr",
                "   print \"y is \"",
                "   print y",
                "   print cr",
                "   print \"z is \"",
                "   print z",
                "end"
        ));
        RudiSourceRegistry.getInstance().put(RudiConstant.MAIN_PROGRAM_KEY, source);

        LineProcessor lp = new DelegatingLineProcessor(Arrays.asList(
                ProgramLineProcessor.getInstance(),
                DecsLineProcessor.getInstace(),
                StartingBracketLineProcessor.getInstance(),
                EndingBracketLineProcessor.getInstance(),
                VariableDeclarationProcessor.getInstance(),
                BeginLineProcessor.getInstance(),
                EndLineProcessor.getInstance(),
                PrintLineProcessor.getInstance(),
                VariableAssignmentProcessor.getInstance()
        ));

        for (int i = 1; i <= source.totalLines(); i++) {
            String line = source.getLine(i);
            lp.doProcess(i, line);
        }

        Assert.assertEquals("x is 2\ny is 3.1\nz is hello", myOut.toString());
    }

    @Test
    public void testSubRoutineWithoutControlStructures() {
        RudiSource source = new RudiSource(Arrays.asList(
                "program",
                "decs",
                "[",
                "   integer x /* an inline comment */",
                "   float y",
                "   string z",
                "]",
                "begin",
                "   x = 2",
                "   y = 1.1 + x",
                "   z = \"hello\"",
                "   foo(x, y)",
                "   print z",
                "end",
                "",
                "/* some comment */",
                "subroutine foo(a, b)",
                "decs",
                "[",
                "   string x",
                "]",
                "begin",
                "   x = \"world\"",
                "   print x",
                "   print cr",
                "   print a",
                "   print cr",
                "   print b",
                "   print cr",
                "return"
        ));
        SourcePreProcessor.process(source);

        RudiSource main = RudiSourceRegistry.getInstance().get(RudiConstant.MAIN_PROGRAM_KEY);
        for (int i = 1; i <= main.totalLines(); i++) {
            DefaultLineProcessor.getInstance().doProcess(i, main.getLine(i));
        }

        Assert.assertEquals("world\n2\n3.1\nhello", myOut.toString());
    }
}
