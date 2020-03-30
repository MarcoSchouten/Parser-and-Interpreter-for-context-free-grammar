/**
 * Classe per la gestione degli operatori aritmetici (pattern Interpreter)
 *
 */
public class ExpOperator implements Expression {
    public enum OperatorTag {
	NOP, ADD, SUB, MUL, DIV
    };

    private OperatorTag operator;
    private Expression left;
    private Expression right;

    public ExpOperator() {
	operator = OperatorTag.NOP;
	left = right = null;
    }

    public ExpOperator(OperatorTag op, Expression op1, Expression op2) {
	operator = op;
	left = op1;
	right = op2;
    }

    public OperatorTag getOperator() {
	return operator;
    }

    public Expression getLeftOperand() {
	return left;
    }

    public Expression getRightOperand() {
	return right;
    }

    public void accept(ExpressionVisitor visitor) {
	visitor.visit(this);
    }
}