
public class SetExpression implements Statement {
    String var;
    Expression exp;

    public SetExpression(Expression e, String v) {
	exp = e;
	var = v;
    }

    public void accept(ExpressionVisitor visitor) {
	visitor.visit(this);
    }

}
