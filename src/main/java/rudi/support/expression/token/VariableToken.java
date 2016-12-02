package rudi.support.expression.token;

import rudi.support.expression.eval.Evaluator;
import rudi.support.literal.Constant;
import rudi.support.variable.Variable;
import rudi.support.variable.VariableAccessor;

/**
 * A variable token in expression
 */
public class VariableToken extends Token {

    private final VariableAccessor accessor;

    public VariableToken(String faceValue, VariableAccessor accessor) {
        super(faceValue);
        this.accessor = accessor;
    }

    public VariableAccessor getAccessor() {
        return accessor;
    }

    @Override
    public boolean isOperand() {
        return true;
    }

    @Override
    public Evaluator evaluator() {
        return ((lhs, rhs) -> {
            Variable var = this.accessor.access();
            Constant c = new Constant(var.getType(), var.getValue());
            return new ConstantToken(c.getValue().toString(), c);
        });
    }
}
