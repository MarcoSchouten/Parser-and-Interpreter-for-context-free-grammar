import java.util.Stack;

/**
 * Visitor concreto per la valutazione di espressioni (pattern Visitor) Visita
 * l'albero e valuta le espressioni utilizzando le definizioni di variabili
 * contenute in un oggetto <code>Context</code>. Valuta le espressioni
 * utilizzando un accumulatore a stack (meccanismo simile a quello utilizzato
 * dalla JVM).
 *
 *
 */
public class EvaluationVisitor implements ExpressionVisitor {
    /**
     * Accumulatore per la valutazione
     */
    private Stack<Long> accumulator;

    /**
     * Tabella dei simboli per valutazione delle variabili
     */
    private Context context;

    /**
     * Costruttore. Non c'è costruttore di default in quanto è necessario un
     * oggetto Context per la valutazione.
     * 
     * @param c l'oggetto Context con il mapping tra variabili e valori
     */
    public EvaluationVisitor(Context c) {
	accumulator = new Stack<Long>();
	context = c;
    }

    public void visit(ExpNumber toVisit) {
	accumulator.push(toVisit.getValue());
    }

    public void visit(Variable toVisit) {
	boolean check = context.containsVariable(toVisit.getName());
	if (check == false) {
	    throw new SyntaxError("(ERROR in evaluator: undefined variable " + toVisit.getName() + ")");

	}

	Long value = context.getVariable(toVisit.getName());
	accumulator.push(value);
    }

    public void visit(ExpOperator toVisit) {
	Expression exprLeft = toVisit.getLeftOperand();
	Expression exprRight = toVisit.getRightOperand();
	exprLeft.accept(this); // propago a sinistra
	exprRight.accept(this); // propago a destra
	long valueRight = accumulator.pop(); // nello stack trovo i valori che mi servono
	long valueLeft = accumulator.pop();
	ExpOperator.OperatorTag op = toVisit.getOperator();
	switch (op) {
	case ADD:
	    accumulator.push(valueLeft + valueRight);
	    break;
	case SUB:
	    accumulator.push(valueLeft - valueRight);
	    break;
	case MUL:
	    accumulator.push(valueLeft * valueRight);
	    break;
	case DIV:
	    if (valueRight == 0) {

		throw new SyntaxError("(ERROR in evaluator: division by zero)");
	    }
	    accumulator.push(valueLeft / valueRight);
	    break;
	case NOP:
	    break;
	}
    }

    /**
     * Metodo per l'interrogazione del valore dell'espressione.
     * 
     * @return Il valore sulla cima dello stack
     */
    public Long getValue() {
	return accumulator.lastElement();
    }

    /**
     * Cancella i contenuti dell'accumulatore interno. Per come funziona
     * l'accumulatore, se un'espressione viene valutata più volte, è l'ultima
     * valutazione quella che viene prelevata da <code>getValue</code>
     */
    public void reset() {
	accumulator.clear();
    }

    public void visit(SetExpression toVisit) {
	toVisit.exp.accept(this);
	context.setVariable(toVisit.var, this.getValue());
	System.out.print(toVisit.var + " = " + this.getValue());
    }

    public void visit(GetExpression toVisit) {
	toVisit.exp.accept(this);
	System.out.print(this.getValue());

    }

}