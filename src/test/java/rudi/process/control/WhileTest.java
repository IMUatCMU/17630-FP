package rudi.process.control;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.process.DefaultLineProcessor;
import rudi.process.pre.SourcePreProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiSource;
import rudi.support.RudiSourceRegistry;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class WhileTest {

    private ByteArrayOutputStream myOut;

    @Before
    public void setup() {
        myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
    }

    @Test
    public void testWhile() {
        RudiSource source = new RudiSource(Arrays.asList(
                "program",
                "decs",
                "[",
                "   integer x",
                "]",
                "begin",
                "   x = 0",
                "   while ( x :le: 5 )",
                "   [",
                "       print x",
                "       x = x + 1",
                "   ]",
                "   print cr",
                "end"
        ));
        SourcePreProcessor.process(source);

        RudiSource main = RudiSourceRegistry.getInstance().get(RudiConstant.MAIN_PROGRAM_KEY);
        for (int i = 1; i <= main.totalLines(); i++) {
            DefaultLineProcessor.getInstance().doProcess(i, main.getLine(i));
        }

        Assert.assertEquals("012345\n", myOut.toString());
    }
}
