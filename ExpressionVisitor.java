/**
 * Interfaccia per oggetti visitor (pattern Visitor). Dichiara un metodo
 * <code>visit</code> specializzato per ogni elemento della gerarchia
 * <code>Expression</code>.
 * 
 */
public interface ExpressionVisitor {
    public void visit(ExpNumber toVisit);

    public void visit(Variable toVisit);

    public void visit(ExpOperator toVisit);

    public void visit(SetExpression toVisit);

    public void visit(GetExpression toVisit);
}