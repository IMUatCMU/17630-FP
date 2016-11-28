package rudi.process.comment;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.process.DelegatingLineProcessor;
import rudi.process.LineProcessor;
import rudi.process.debug.DebugCollectingLineProcessor;
import rudi.support.RudiContext;
import rudi.support.RudiSource;
import rudi.support.RudiStack;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class CommentLineProcessorsTest {

    private LineProcessor processor;
    private DebugCollectingLineProcessor debugProcessor;

    @Before
    public void setup() {
        this.debugProcessor = new DebugCollectingLineProcessor();
        this.processor = new DelegatingLineProcessor(Arrays.asList(
            StartCommentModeLineProcessor.getInstance(),
                EndCommentModeLineProcessor.getInstance(),
                IgnoreCommentLineProcessor.getInstance(),
                this.debugProcessor
        ));
        RudiStack.getInstance().push(RudiContext.defaultContext());
    }

    @After
    public void clean() {
        RudiStack.getInstance().removeAllElements();
    }

    @Test
    public void testSingleLineOfComment() {
        RudiSource source = new RudiSource(Arrays.asList(
                "/* This is a comment */",
                "program"
        ));
        for (int i = 1; i <= source.totalLines(); i++) {
            this.processor.doProcess(i, source.getLine(i));
        }

        Assert.assertEquals(1, this.debugProcessor.getLines().size());
        Assert.assertEquals("program", this.debugProcessor.getLines().get(0).getSource());
    }

    @Test
    public void testTwoLinesOfComment() {
        RudiSource source = new RudiSource(Arrays.asList(
                "/* This is a",
                "two line comment */",
                "program"
        ));
        for (int i = 1; i <= source.totalLines(); i++) {
            this.processor.doProcess(i, source.getLine(i));
        }

        Assert.assertEquals(1, this.debugProcessor.getLines().size());
        Assert.assertEquals("program", this.debugProcessor.getLines().get(0).getSource());
    }

    @Test
    public void testMoreThanTwoLinesOfComment() {
        RudiSource source = new RudiSource(Arrays.asList(
                "/* This is a",
                "three line ",
                "comment */",
                "program"
        ));
        for (int i = 1; i <= source.totalLines(); i++) {
            this.processor.doProcess(i, source.getLine(i));
        }

        Assert.assertEquals(1, this.debugProcessor.getLines().size());
        Assert.assertEquals("program", this.debugProcessor.getLines().get(0).getSource());
    }
}
