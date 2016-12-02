package rudi.support.expression.token;

import rudi.error.FailedToEvaluateExpressionException;
import rudi.support.RudiConstant;
import rudi.support.expression.eval.Evaluator;
import rudi.support.literal.Constant;
import rudi.support.variable.VarType;
import rudi.support.variable.Variable;
import rudi.support.variable.VariableAccessor;

/**
 * An arithmetic operation token in expression
 */
public class ArithmeticOperatorToken extends Token {

    public ArithmeticOperatorToken(String faceValue) {
        super(faceValue);
    }

    public boolean isAddition() {
        return RudiConstant.ADD.equals(faceValue.toLowerCase());
    }

    public boolean isSubtraction() {
        return RudiConstant.MINUS.equals(faceValue.toLowerCase());
    }

    public boolean isMultiplication() {
        return RudiConstant.MULTIPLY.equals(faceValue.toLowerCase());
    }

    public boolean isDivision() {
        return RudiConstant.DIVIDE.equals(faceValue.toLowerCase());
    }

    @Override
    public boolean isOperand() {
        return false;
    }

    @Override
    public int priority() {
        if (isMultiplication()) {
            return 1003;
        } else if (isDivision()) {
            return 1002;
        } else if (isAddition()) {
            return 1001;
        } else if (isSubtraction()) {
            return 1000;
        } else {
            return 0;
        }
    }

    @Override
    public Evaluator evaluator() {
        return ((lhs, rhs) -> {
            if (!lhs.isOperand() || !rhs.isOperand())
                throw new FailedToEvaluateExpressionException("Evaluation failed for <" + this.faceValue + "> due to not enough operands.");

            Constant c1 = ((ConstantToken) lhs.evaluator().evaluate(null, null)).getConstant();
            Constant c2 = ((ConstantToken) rhs.evaluator().evaluate(null, null)).getConstant();

            if (VarType.INTEGER == c1.getType() && VarType.INTEGER == c2.getType()) {
                Integer result = null;
                if (isAddition()) {
                    result = (Integer) c1.getValue() + (Integer) c2.getValue();
                } else if (isSubtraction()) {
                    result = (Integer) c1.getValue() - (Integer) c2.getValue();
                } else if (isMultiplication()) {
                    result = (Integer) c1.getValue() * (Integer) c2.getValue();
                } else if (isDivision()) {
                    result = (Integer) c1.getValue() / (Integer) c2.getValue();
                }
                return new ConstantToken(
                        result.toString(),
                        new Constant(VarType.INTEGER, result));
            } else if (VarType.FLOAT == c1.getType() || VarType.FLOAT == c2.getType()) {
                Float result = null;
                if (isAddition()) {
                    result = Float.valueOf(c1.getValue().toString()) + Float.valueOf(c2.getValue().toString());
                } else if (isSubtraction()) {
                    result = Float.valueOf(c1.getValue().toString()) - Float.valueOf(c2.getValue().toString());
                } else if (isMultiplication()) {
                    result = Float.valueOf(c1.getValue().toString()) * Float.valueOf(c2.getValue().toString());
                } else if (isDivision()) {
                    result = Float.valueOf(c1.getValue().toString()) / Float.valueOf(c2.getValue().toString());
                }
                return new ConstantToken(
                        result.toString(),
                        new Constant(VarType.FLOAT, result));
            } else {
                throw new FailedToEvaluateExpressionException("Cannot perform <" + this.faceValue + "> on type <" + c1.getType() + "> and type < " + c2.getType() + ">");
            }
        });
    }
}
