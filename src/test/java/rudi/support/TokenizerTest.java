package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.support.expression.token.Tokenizer;

import java.util.List;

@RunWith(JUnit4.class)
public class TokenizerTest {

    @Test
    public void testSimpleArithmetic() {
        List<String> tokens = new Tokenizer("( a + b ) * 4").allTokens();
        Assert.assertEquals(7, tokens.size());
        Assert.assertEquals("(", tokens.get(0));
        Assert.assertEquals("a", tokens.get(1));
        Assert.assertEquals("+", tokens.get(2));
        Assert.assertEquals("b", tokens.get(3));
        Assert.assertEquals(")", tokens.get(4));
        Assert.assertEquals("*", tokens.get(5));
        Assert.assertEquals("4", tokens.get(6));
    }

    @Test
    public void testLogicalRelational() {
        List<String> tokens = new Tokenizer("( a :eq: \"hello world\" ) ^ ( b :lt: 4 )").allTokens();
        Assert.assertEquals(11, tokens.size());
        Assert.assertEquals("(", tokens.get(0));
        Assert.assertEquals("a", tokens.get(1));
        Assert.assertEquals(":eq:", tokens.get(2));
        Assert.assertEquals("\"hello world\"", tokens.get(3));
        Assert.assertEquals(")", tokens.get(4));
        Assert.assertEquals("^", tokens.get(5));
        Assert.assertEquals("(", tokens.get(6));
        Assert.assertEquals("b", tokens.get(7));
        Assert.assertEquals(":lt:", tokens.get(8));
        Assert.assertEquals("4", tokens.get(9));
        Assert.assertEquals(")", tokens.get(10));
    }

    @Test
    public void testOnlyLiteralString() {
        List<String> tokens = new Tokenizer("\"hello world !!!\"").allTokens();
        Assert.assertEquals(1, tokens.size());
        Assert.assertEquals("\"hello world !!!\"", tokens.get(0));
    }
}
