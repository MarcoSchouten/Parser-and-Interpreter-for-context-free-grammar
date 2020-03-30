
public class Variable implements Token, Expression {
    /**
     * member variables
     */
    private String value;

    public Variable() {
	value = null;
    }

    public Variable(String value) {
	this.value = value;
    }

    /**
     * method from token
     */
    public String getValue() {
	return this.value;
    }

    public String getType() {
	return "variable";
    }

    /**
     * method form Expression
     * 
     * @return
     */
    public String getName() {
	return value;
    }

    public void accept(ExpressionVisitor visitor) {
	visitor.visit(this);
    }
}
