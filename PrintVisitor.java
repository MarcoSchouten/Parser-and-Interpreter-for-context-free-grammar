/**
 * Visitor concreto per la stampa di espressioni in sintassi LISP-like (pattern
 * Visitor)
 * 
 *
 */
public class PrintVisitor implements ExpressionVisitor {

    public PrintVisitor() {
    }

    public void visit(ExpNumber toVisit) {
	System.out.print(toVisit.getValue());
    }

    public void visit(Variable toVisit) {
	System.out.print(toVisit.getName());
    }

    public void visit(ExpOperator toVisit) {
	System.out.print("(");
	ExpOperator.OperatorTag op = toVisit.getOperator();
	switch (op) {
	case ADD:
	    System.out.print("ADD ");
	    break;
	case SUB:
	    System.out.print("SUB ");
	    break;
	case MUL:
	    System.out.print("MUL ");
	    break;
	case DIV:
	    System.out.print("DIV ");
	    break;
	case NOP:
	    break;
	}
	Expression exprLeft = toVisit.getLeftOperand();
	Expression exprRight = toVisit.getRightOperand();
	exprLeft.accept(this);
	System.out.print(" ");
	exprRight.accept(this);
	System.out.print(")");
    }

    public void visit(SetExpression toVisit) {

	System.out.print("(SET ");
	toVisit.exp.accept(this);
	System.out.print(")");

    }

    public void visit(GetExpression toVisit) {
	// System.out.println("sto visitando expression della GET");
	System.out.print("(GET ");
	toVisit.exp.accept(this);
	System.out.print(")");
    }

}