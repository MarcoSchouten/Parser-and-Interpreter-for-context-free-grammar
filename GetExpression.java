
public class GetExpression implements Statement {
    Expression exp;

    public GetExpression(Expression exp) {
	this.exp = exp;
    }

    public void accept(ExpressionVisitor visitor) {
	visitor.visit(this);
    }

}
