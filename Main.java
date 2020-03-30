import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 
 * 1 analisi lessicale - lexer: stream di chars --> lista di token 
 * 2 analisi sintattica - parser: lista di token --> albero. 
 * 3 analisi semantica - visitor: albero --> result
 */
public class Main {
    public static void main(String[] args) {
	try {
	    // 1 analisi lessicale
	    Lexer myLexer = new Lexer(args[0]);
	    TokenList input = myLexer.generateTokenList1();

	    // 2 analisi sintattica
	    Context myContext = new Context();
	    Parser myParser = new Parser(input);
	    ArrayList<Statement> statementList = myParser.parse();

	    // 3 analisi semantica
	    /*
	     * for (Statement s : statementList) { PrintVisitor printVisitor = new
	     * PrintVisitor(); System.out.print("BuiltExpression:"); s.accept(printVisitor);
	     * System.out.println(); }
	     */

	    for (Statement s : statementList) {
		EvaluationVisitor evalVisitor = new EvaluationVisitor(myContext);
		// System.out.print("Evaluation:");
		s.accept(evalVisitor);
		System.out.println();
	    }
	} catch (SyntaxError | FileNotFoundException e) {
	    return;
	}
    }
}
