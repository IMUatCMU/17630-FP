package rudi.support;

import rudi.error.DuplicateVariableDeclarationException;
import rudi.error.VariableNotInRegistrarException;
import rudi.support.literal.Constant;
import rudi.support.variable.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * All stateful information related to a single evaluation stack.
 * For instance, the main program will have its own context, a
 * subroutine will have its own context as well.
 */
public class RudiContext implements VariableRegistrar {

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

    public RudiContext contextInheritingVariablesAndParameters() {
        RudiContext newCtx = defaultContext();
//        this.variableRegistrar.forEach((s, variable) -> {
//            newCtx.paramRegistrar.put(s, new ModifyAccessPair(
//                    this.modifier(s),
//                    this.accessor(s)
//            ));
//        });
        newCtx.variableRegistrar.putAll(this.variableRegistrar);
        newCtx.paramRegistrar.putAll(this.paramRegistrar);
        //newCtx.paramRegistrar.putAll(this.paramRegistrar);
        return newCtx;
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

    // =================================================================================================================
    // variable registrar
    // =================================================================================================================

    private Map<String, Variable> variableRegistrar = new HashMap<>();
    private final Map<String, ModifyAccessPair> paramRegistrar = new HashMap<>();

    public Map<String, ModifyAccessPair> getParamRegistrar() {
        return paramRegistrar;
    }

    @Override
    public void declare(Variable variable) {
        assert variable.getType() != null;
        assert variable.getName() != null && variable.getName().length() > 0;

        if (this.paramRegistrar.containsKey(variable.getName()))
            throw new DuplicateVariableDeclarationException(variable);
        else if (this.variableRegistrar.containsKey(variable.getName()))
            throw new DuplicateVariableDeclarationException(variable);

        this.variableRegistrar.put(variable.getName(), variable);
    }

    @Override
    public VariableModifier modifier(String name) {
        if (this.paramRegistrar.containsKey(name))
            return this.paramRegistrar.get(name).getModifier();
        else if (this.variableRegistrar.containsKey(name))
            return new VariableModifierImpl(name, this.variableRegistrar);

        throw new VariableNotInRegistrarException(name);
    }

    @Override
    public VariableAccessor accessor(String name) {
        if (this.paramRegistrar.containsKey(name))
            return this.paramRegistrar.get(name).getAccessor();
        else if (this.variableRegistrar.containsKey(name))
            return new VariableAccessorImpl(name, this.variableRegistrar);

        throw new VariableNotInRegistrarException(name);
    }

    // =================================================================================================================
    // comment
    // =================================================================================================================

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

    // =================================================================================================================
    // bracketDepth
    // =================================================================================================================

    /**
     * Record the level of bracket we are in. Should start
     * with 0 and end with 0
     */
    private int bracketDepth = 0;

    public int getBracketDepth() {
        return bracketDepth;
    }

    public void setBracketDepth(int bracketDepth) {
        this.bracketDepth = bracketDepth;
    }

    public void increaseBracketDepth() {
        bracketDepth++;
    }

    public void decreaseBracketDepth() {
        bracketDepth--;
    }

    // =================================================================================================================
    // declarationMode
    // =================================================================================================================

    /**
     * A boolean flag indicating we are in the middle
     * of declaring variables.
     */
    private boolean declarationMode = false;

    public boolean isDeclarationMode() {
        return declarationMode;
    }

    public void setDeclarationMode(boolean declarationMode) {
        this.declarationMode = declarationMode;
    }

    // =================================================================================================================
    // executionMode
    // =================================================================================================================

    /**
     * A boolean flag indicating we are in the middle
     * of executing statements.
     */
    private boolean executionMode = false;

    public boolean isExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(boolean executionMode) {
        this.executionMode = executionMode;
    }

    // =================================================================================================================
    // ifThenElseCount
    // =================================================================================================================

    private int ifThenElseCount = 0;

    public int getIfThenElseCount() {
        return ifThenElseCount;
    }

    public void setIfThenElseCount(int ifThenElseCount) {
        this.ifThenElseCount = ifThenElseCount;
    }

    // =================================================================================================================
    // Control structure
    // =================================================================================================================

    private Constant controlCondition = null;
    private RudiSource trueSource = null;
    private RudiSource falseSource = new RudiSource(new ArrayList<>());
    private ControlBranch controlBranch = null;         // indicates the branch we are currently in
    private boolean skipMode = false;                   // entering criteria for SkipLineProcessor
    private int branchStartLineNumber = 0;
    private int branchEndLineNumber = 0;

    public enum ControlBranch {
        TRUE, FALSE
    }

    public Constant getControlCondition() {
        return controlCondition;
    }

    public void setControlCondition(Constant controlCondition) {
        this.controlCondition = controlCondition;
    }

    public ControlBranch getControlBranch() {
        return controlBranch;
    }

    public void setControlBranch(ControlBranch controlBranch) {
        this.controlBranch = controlBranch;
    }

    public boolean isSkipMode() {
        return skipMode;
    }

    public void setSkipMode(boolean skipMode) {
        this.skipMode = skipMode;
    }

    public int getBranchStartLineNumber() {
        return branchStartLineNumber;
    }

    public void setBranchStartLineNumber(int branchStartLineNumber) {
        this.branchStartLineNumber = branchStartLineNumber;
    }

    public int getBranchEndLineNumber() {
        return branchEndLineNumber;
    }

    public void setBranchEndLineNumber(int branchEndLineNumber) {
        this.branchEndLineNumber = branchEndLineNumber;
    }

    public RudiSource getTrueSource() {
        return trueSource;
    }

    public void setTrueSource(RudiSource trueSource) {
        this.trueSource = trueSource;
    }

    public RudiSource getFalseSource() {
        return falseSource;
    }

    public void setFalseSource(RudiSource falseSource) {
        this.falseSource = falseSource;
    }
}
