package rudi.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.error.DuplicateVariableDeclarationException;
import rudi.support.variable.ModifyAccessPair;
import rudi.support.variable.VarType;
import rudi.support.variable.Variable;

@RunWith(JUnit4.class)
public class VariableRegistryTest {

    @Test
    public void testDeclareVariableInCurrentContext() {
        RudiContext ctx = RudiContext.defaultContext();
        RudiStack.getInstance().push(ctx);

        Variable var = new Variable(VarType.STRING, "foo");
        RudiStack.getInstance().peek().declare(var);

        Assert.assertNotNull(RudiStack.getInstance().peek().modifier("foo"));
        Assert.assertNotNull(RudiStack.getInstance().peek().accessor("foo"));
    }

    @Test(expected = DuplicateVariableDeclarationException.class)
    public void testDeclareDuplicateVariableInCurrentContext() {
        RudiContext ctx = RudiContext.defaultContext();
        RudiStack.getInstance().push(ctx);
        Variable var = new Variable(VarType.STRING, "foo");
        RudiStack.getInstance().peek().declare(var);

        RudiStack.getInstance().peek().declare(var);
    }

    @Test(expected = DuplicateVariableDeclarationException.class)
    public void testDeclareDuplicateVariableCollidingWithParams() {
        Variable foo = new Variable(VarType.STRING, "foo");

        RudiContext parentCtx = RudiContext.defaultContext();
        parentCtx.declare(foo);
        RudiStack.getInstance().push(parentCtx);

        RudiContext childCtx = RudiContext.defaultContext();
        childCtx.getParamRegistrar().put("bar", new ModifyAccessPair(
                parentCtx.modifier("foo"),
                parentCtx.accessor("foo")
        ));
        childCtx.setParentContext(parentCtx);
        RudiStack.getInstance().push(childCtx);

        childCtx.declare(new Variable(VarType.INTEGER, "bar"));
    }

    @Test
    public void testModifyVariableInCurrentContext() {
        Variable var = new Variable(VarType.STRING, "foo");

        RudiContext ctx = RudiContext.defaultContext();
        ctx.declare(var);
        RudiStack.getInstance().push(ctx);

        Assert.assertNull(ctx.accessor("foo").access().getValue());
        ctx.modifier("foo").modify("bar");
        Assert.assertEquals("bar", ctx.accessor("foo").access().getValue());
    }

    @Test
    public void testModifyParameter() {
        Variable foo = new Variable(VarType.STRING, "foo");

        RudiContext parentCtx = RudiContext.defaultContext();
        parentCtx.declare(foo);
        RudiStack.getInstance().push(parentCtx);

        RudiContext childCtx = RudiContext.defaultContext();
        childCtx.getParamRegistrar().put("bar", new ModifyAccessPair(
                parentCtx.modifier("foo"),
                parentCtx.accessor("foo")
        ));
        childCtx.setParentContext(parentCtx);
        RudiStack.getInstance().push(childCtx);

        RudiStack.getInstance().peek().modifier("bar").modify("Hello World");
        Assert.assertEquals("Hello World", parentCtx.accessor("foo").access().getValue());
    }
}
