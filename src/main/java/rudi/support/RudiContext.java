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

    /**
     * Create a new context that has the same variable/parameters of
     * this context. Useful when creating contexts for
     * @return
     */
    public RudiContext contextInheritingVariablesAndParameters() {
        RudiContext newCtx = defaultContext();
        newCtx.variableRegistrar.putAll(this.variableRegistrar);
        newCtx.paramRegistrar.putAll(this.paramRegistrar);
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

    /**
     * Set to true when exits declaration mode and
     * should prevent re-enter
     */
    private boolean declarationConcluded = false;

    public boolean isDeclarationMode() {
        return declarationMode;
    }

    public void setDeclarationMode(boolean declarationMode) {
        this.declarationMode = declarationMode;
    }

    public boolean isDeclarationConcluded() {
        return declarationConcluded;
    }

    public void setDeclarationConcluded(boolean declarationConcluded) {
        this.declarationConcluded = declarationConcluded;
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
    // Control structure
    // =================================================================================================================

    private int controlExpressionBracketDepth = 0;      // the bracket depth on the control expression line
    private int controlExpressionLineNumber = 0;        // line number of the control expression line
    private String controlExpression = null;            // text of the control expression
    private ControlType controlType = null;             // IF or WHILE
    private RudiSource trueSource = null;               // true branch source
    private RudiSource falseSource = new RudiSource(new ArrayList<>());     // false branch source, defaulted to empty in absence of 'else'
    private ControlBranch controlBranch = null;         // indicates the branch we are currently in
    private boolean skipMode = false;                   // entering criteria for SkipLineProcessor
    private int branchStartLineNumber = 0;              // first line of branch source
    private int branchEndLineNumber = 0;                // last line of branch source

    public enum ControlBranch {
        TRUE, FALSE
    }

    public enum ControlType {
        IF, WHILE
    }

    public int getControlExpressionBracketDepth() {
        return controlExpressionBracketDepth;
    }

    public void setControlExpressionBracketDepth(int controlExpressionBracketDepth) {
        this.controlExpressionBracketDepth = controlExpressionBracketDepth;
    }

    public int getControlExpressionLineNumber() {
        return controlExpressionLineNumber;
    }

    public void setControlExpressionLineNumber(int controlExpressionLineNumber) {
        this.controlExpressionLineNumber = controlExpressionLineNumber;
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

    public String getControlExpression() {
        return controlExpression;
    }

    public void setControlExpression(String controlExpression) {
        this.controlExpression = controlExpression;
    }

    public ControlType getControlType() {
        return controlType;
    }

    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
    }
}
