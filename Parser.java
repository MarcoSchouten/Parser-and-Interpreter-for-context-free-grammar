import java.util.ArrayList;

public class Parser {
    /**
     * variables member
     */
    private ArrayList<TokenList> lexerText; // ogni nodo è una tokenlist senza END

    private ArrayList<Statement> statementList; // ogni nodo è un albero ben formato
    private Expression expression;

    /**
     * constructor
     * 
     */
    public Parser(TokenList input_lexer) {
	this.lexerText = new ArrayList<TokenList>();
	readInput(input_lexer);

	statementList = new ArrayList<Statement>();
	expression = null;

    }

    /**
     * parse every statement
     * 
     * @return ArrayList<Statement>
     */
    public ArrayList<Statement> parse() {

	for (int i = 0; i < lexerText.size(); i++) {

	    // check formato (GET | SET exp )
	    if (!((lexerText.get(i).getToken(0).getValue().equals(SpecialSymbol.tag.OPEN_PARENTHESIS.toString()))
		    && ((lexerText.get(i).getToken(1).getValue() == "SET")
			    || lexerText.get(i).getToken(1).getValue() == "GET")
		    && (lexerText.get(i).getToken(lexerText.get(i).getLength() - 1).getValue()
			    .equals(SpecialSymbol.tag.CLOSE_PARENTHESIS.toString())))) {
		// System.out.println();
		throw new SyntaxError("(ERROR in parser: premature end of input)");
	    }

	    // calcolo il parse
	    Statement statement = doParse(lexerText.get(i));

	    // check 12 prende il primo elemento dello statement successivo
	    if (statement == null) {
		// System.out.println();
		throw new SyntaxError("(ERROR in parser: expected right parenthesis, got '"
			+ adapt(lexerText.get(i + 1).getToken(0).getValue()) + "')");
	    }

	    statementList.add(statement);
	}
	return statementList;
    }

    private Statement doParse(TokenList text) {
	String type = getType(text);

	// caso get
	if (type.equals("GET")) {
	    // check12
	    Expression e = parseExpression(text, 2);
	    if (e == null)
		return null;

	    Statement statement = new GetExpression(e);
	    return statement;

	    // caso set
	} else if (type.equals("SET")) {
	    // check sulla vaiabile
	    if (!(text.getToken(2).getType().equals("variable"))) {
		// System.out.println( );
		throw new SyntaxError(
			"(ERROR in parser: expected variable name, got '" + adapt(text.getToken(2).getValue()) + "')");
	    }
	    // check 12
	    Expression e = parseExpression(text, 3);
	    if (e == null)
		return null;
	    Statement statement = new SetExpression(e, text.getToken(2).getValue().toString());
	    return statement;
	}
	return null;
    }

    private Expression parseExpression(TokenList text, int index) throws SyntaxError {
	int i = recursiveParseExpression(text, index);

	// finisco prima
	if (i < text.getLength() - 1) {
	    // System.out.println();
	    throw new SyntaxError("(ERROR in parser: missing declaration or evaluation statement)");
	}

	// DEVO AVERE UNA PARENTESI 
	if (i == text.getLength() - 1) {
	    if (!(text.getToken(i).getValue().equals(SpecialSymbol.tag.CLOSE_PARENTHESIS.toString()))) {
		// System.out.println();
		throw new SyntaxError("(ERROR in parser: expected right parenthesis, got '"
			+ adapt(text.getToken(i).getValue()) + ")");
	    }
	}

	// mi aspettavo una parentesi ')' invece devo stampare il primo elemento dello
	// statement successivo
	if (i == text.getLength()) {
	    return null;
	}

	// System.out.println("i: " + i);
	// System.out.println("text_length: " + text.getLength());
	// System.out.println("last_token: " + text.getToken(i).getValue().toString());

	return expression;
    }

    private int recursiveParseExpression(TokenList text, int i) {
	// System.out.println("chiamata:" + i);
	// base case 1
	if (text.getToken(i).getType().equals("constant")) {
	    expression = new ExpNumber(Long.parseLong(text.getToken(i).getValue()));
	    // System.out.println("ho trovato una costante:" + text.getToken(i).getValue());
	    return i + 1;
	}
	// base case 2
	else if (text.getToken(i).getType().equals("variable")) {

	    if (isKeyword(text.getToken(i).getValue()))
		throw new SyntaxError("ERROR in parser: you can not assign KeyWord name to a variable");

	    expression = new Variable(text.getToken(i).getValue());
	    // System.out.println("ho trovato una var:" + text.getToken(i).getValue());
	    return i + 1;
	}
	// complex case
	else if (text.getToken(i).getValue().equals(SpecialSymbol.tag.OPEN_PARENTHESIS.toString())) {
	    // System.out.println("ho trovato un'exp");
	    i++; // dopo la parentesi c'é sempre una keyword

	    // check keyword
	    if (!isKeyword(text.getToken(i).getValue())) {
		// System.out.println();
		throw new SyntaxError("(ERROR in parser: unrecognized operator '" + text.getToken(i).getValue() + "')");
	    }

	    // add key
	    ExpOperator.OperatorTag op = ExpOperator.OperatorTag.valueOf(text.getToken(i).getValue());
	    // System.out.println("ho aggiunto keyword:" + text.getToken(i).getValue());

	    // ad op1
	    i = recursiveParseExpression(text, i + 1);
	    Expression left = expression;

	    // add op2
	    i = recursiveParseExpression(text, i);
	    Expression right = expression;

	    if (i >= text.getLength()) {
		// System.out.println();
		throw new SyntaxError("(ERROR in parser: premature end of input");
	    }
	    expression = new ExpOperator(op, left, right);

	    return (i + 1);

	} else {
	    // System.out.println();
	    throw new SyntaxError("(ERROR in parser: misplaced token '" + adapt(text.getToken(i).getValue()) + "')");
	}
    }

    private boolean isKeyword(String value) {
	for (ExpOperator.OperatorTag op : ExpOperator.OperatorTag.values()) {
	    if (value.equalsIgnoreCase(op.toString()))
		return true;
	}
	return false;
    }

    private String getType(TokenList text) {
	if (text.getToken(1).getValue().equalsIgnoreCase("GET"))
	    return "GET";
	else if (text.getToken(1).getValue().equalsIgnoreCase("SET"))
	    return "SET";
	else
	    return "undefined";
    }

    private void readInput(TokenList input) {
	int index = 0;
	while (index < input.getLength()) {
	    TokenList myStatement = new TokenList();
	    while (input.getToken(index).getValue() != SpecialSymbol.tag.END.toString()) {
		myStatement.addToken(input.getToken(index)); // aggiunge un elemento all'albero diverso da END
		index++;
	    }
	    // adesso input è posizionato su END e lo scavalco
	    index++;
	    lexerText.add(myStatement); // aggiunge un albero alla lista
	}
    }

    public void print() {
	for (TokenList tl : lexerText) {
	    System.out.println("statement:");
	    tl.printList();
	    System.out.println(" ");
	    System.out.println(" ");
	}
    }

    /**
     * dummy method che serve per avere stampato ESATTAMENTE l'output come richiesto
     * dal contratto della consegna.
     * 
     * @param value
     * @return
     */
    private String adapt(String value) {
	if (value.equals("OPEN_PARENTHESIS"))
	    return "(";
	else if (value.equals("CLOSE_PARENTHESIS"))
	    return ")";
	return value;
    }
}
