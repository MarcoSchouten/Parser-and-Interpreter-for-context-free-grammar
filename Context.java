import java.util.HashMap;

/**
 * Classe per la gestione del contesto (pattern Interpreter)
 * Si tratta di un Wrapper per una tabella dei simboli
 *
 */
public class Context {

    private HashMap<String, Long> symbolTable;

    public Context() {
	symbolTable = new HashMap<String, Long>();
    }

    public void setVariable(String id, long value) {
	symbolTable.put(id, value);
    }

    public Long getVariable(String id) {
	return symbolTable.get(id);
    }

    public boolean containsVariable(String id) {
	return symbolTable.containsKey(id);
    }

}