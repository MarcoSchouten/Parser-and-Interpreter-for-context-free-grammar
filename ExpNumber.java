
/**
 * Classe per la gestione di valori numerici interi (pattern Interpreter)
 *
 */
public class ExpNumber implements Expression {
    /**
     * member variables
     */
    private long value;

    /**
     * methods
     */
    public ExpNumber() {
	value = 0;
    }

    public ExpNumber(long l) {
	value = l;
    }

    public long getValue() {
	return value;
    }

    public void setValue(long v) {
	value = v;
    }

    public void accept(ExpressionVisitor visitor) {
	visitor.visit(this);
    }
}
