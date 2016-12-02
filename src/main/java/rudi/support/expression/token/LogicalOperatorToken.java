package rudi.support.expression.token;

import rudi.error.FailedToEvaluateExpressionException;
import rudi.support.RudiConstant;
import rudi.support.expression.eval.Evaluator;
import rudi.support.literal.Constant;
import rudi.support.variable.VarType;

/**
 * A logical operator in expression
 */
public class LogicalOperatorToken extends Token {

    public LogicalOperatorToken(String faceValue) {
        super(faceValue);
    }

    public boolean isAnd() {
        return RudiConstant.AND.equals(faceValue.toLowerCase());
    }

    public boolean isOr() {
        return RudiConstant.OR.equals(faceValue.toLowerCase());
    }

    public boolean isNot() {
        return RudiConstant.NOT.equals(faceValue.toLowerCase());
    }

    @Override
    public boolean isOperand() {
        return false;
    }

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public boolean isParenthesis() {
        return false;
    }

    @Override
    public int priority() {
        if (isNot()) {
            return 12;
        } else if (isAnd()) {
            return 11;
        } else if (isOr()) {
            return 10;
        } else {
            return 0;
        }
    }

    /**
     * Don't forget to pass a dummy boolean token as rhs in the case of 'not' operator
     * @return
     */
    @Override
    public Evaluator evaluator() {
        return ((lhs, rhs) -> {
            if (!lhs.isOperand() || !rhs.isOperand())
                throw new FailedToEvaluateExpressionException("Evaluation failed for <" + this.faceValue + "> due to not enough operands.");

            Constant c1 = ((ConstantToken) lhs.evaluator().evaluate(null, null)).getConstant();
            Constant c2 = ((ConstantToken) rhs.evaluator().evaluate(null, null)).getConstant();

            if (VarType.BOOLEAN != c1.getType() || VarType.BOOLEAN != c2.getType()) {
                throw new FailedToEvaluateExpressionException("Cannot perform <" + this.faceValue + "> on non-boolean type");
            }

            if (isAnd()) {
                Boolean result = (Boolean) c1.getValue() && (Boolean) c2.getValue();
                return new ConstantToken(result.toString(), new Constant(VarType.BOOLEAN, result));
            } else if (isOr()) {
                Boolean result = (Boolean) c1.getValue() || (Boolean) c2.getValue();
                return new ConstantToken(result.toString(), new Constant(VarType.BOOLEAN, result));
            } else if (isNot()) {
                Boolean result = !((Boolean) c1.getValue());
                return new ConstantToken(result.toString(), new Constant(VarType.BOOLEAN, result));
            } else {
                throw new FailedToEvaluateExpressionException("Unsupported operator <" + this.faceValue + ">");
            }
        });
    }
}
