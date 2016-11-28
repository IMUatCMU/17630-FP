package rudi.support;

/**
 * All stateful information related to a single evaluation stack.
 * For instance, the main program will have its own context, a
 * subroutine will have its own context as well.
 */
public class RudiContext {

    // =================================================================================================================
    // Create RudiContext
    // =================================================================================================================

    /**
     * Create an empty context with all defaults.
     * @return
     */
    public static RudiContext defaultContext() {
        return new RudiContext();
    }

    // =================================================================================================================
    // parentContext
    // =================================================================================================================

    /**
     * The context which this context spawned from. This is useful to separate
     * the variable scopes while still keeping the possibility to access parent
     * scope if necessary. For instance, if parent context has variables X and this
     * context has variable Y. In this context, we do "X = Y". You are not gonna
     * find registration of variable X in this scope, in which case you need to consult
     * parent scope and access the variable modifier from the parent scope.
     */
    private RudiContext parentContext = null;

    public RudiContext getParentContext() {
        return parentContext;
    }

    public void setParentContext(RudiContext parentContext) {
        this.parentContext = parentContext;
    }

    // =================================================================================================================
    // source
    // =================================================================================================================

    /**
     * Source code for this context.
     */
    private RudiSource sourceCode = null;

    public RudiSource getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(RudiSource sourceCode) {
        this.sourceCode = sourceCode;
    }

    /**
     * A boolean flag indicating whether we are in comment
     * mode. If true, the interpreter is supposed to ignore
     * the current line.
     */
    private boolean comment = false;

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }
}
