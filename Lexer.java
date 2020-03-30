import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * il suo compito e' leggere il file e generare una lista di token
 * 
 * @author marco
 *
 */

public class Lexer {
    private FileReader file;
    private Scanner sc;
    private TokenList myList;

    /**
     * constructor
     * 
     * @param file_path
     * @throws FileNotFoundException
     */
    public Lexer(String file_path) throws FileNotFoundException {
	// pass the path to the file as a parameter
	file = new FileReader(file_path);
	sc = new Scanner(file);
	myList = new TokenList();
    }

    /**
     * tokenizzo tutto
     * 
     * @return
     */
    public TokenList generateTokenList1() {
	StringBuffer text_line = new StringBuffer();
	while (sc.hasNextLine()) {
	    String str = sc.nextLine();
	    str = str.replace("\n", " ").replace("\r", " ").replace("\t", " ");
	    text_line.append(str);
	}
	// System.out.println("sto tokenizzando:" + text_line);

	tokenize(text_line);
	return myList;
    }

    /**
     * data la una stringa in input, aggiunge i token alla lista
     * 
     * @param text_line
     */
    private void tokenize(StringBuffer text_line) {
	if (text_line.length() != 0) {
	    // System.out.println("text_line.value:" + text_line);
	    // System.out.println("lunghezza:" + text_line.length());

	    for (int i = 0; i < text_line.length(); i++) {
		// System.out.println("posizione:" + i + " check.value:" + text_line.charAt(i));

		check_undefined_char(text_line, i);
		i = i + check_special_symbol(text_line, i);
		i = i + check_word(text_line, i);
		i = i + check_number(text_line, i);

	    }
	    // System.out.println("sto SEPARANDO:");
	    // separo i vari statement
	    for (int i = 0; i < myList.getLength(); i++) {
		if ((myList.getToken(i).getValue().equals(Keyword.KeywordTag.GET.toString()))
			|| (myList.getToken(i).getValue().equals(Keyword.KeywordTag.SET.toString()))) {
		    Token t = new SpecialSymbol(SpecialSymbol.tag.END);
		    if ((i - 1) != 0) {
			myList.insertInto(i - 1, t);
			i++;
		    }
		}

	    }
	    Token t = new SpecialSymbol(SpecialSymbol.tag.END);
	    myList.addToken(t);

	}
    }

    private void check_undefined_char(StringBuffer text_line, int i) {
	char c = text_line.charAt(i);
	if ((c != ' ') && (c != '(') && (c != ')') && (c != '-') && isChar(c) == false
		&& Character.isDigit(c) == false) {
	    // System.out.println();
	    throw new SyntaxError("(ERROR in tokenizer: stray character '" + c + "')");
	}

    }

    private int check_word(StringBuffer text_line, int i) {
	char c = text_line.charAt(i);

	if (isChar(c)) {
	    StringBuffer temp = new StringBuffer();

	    while ((i < text_line.length()) && (isChar(text_line.charAt(i)))) {
		// System.out.println("adding_to_temp:" + text_line.charAt(i));
		temp.append(text_line.charAt(i));
		// System.out.println("temp:" + temp);
		i++;
	    }
	    // System.out.println("temp:");

	    for (Keyword.KeywordTag myTag : Keyword.KeywordTag.values()) {
		if (temp.toString().equals(myTag.toString())) {
		    // is keyword
		    Token t = new Keyword(myTag);
		    myList.addToken(t);
		    // temp.delete(0, temp.length()); // azzero il buff
		    return 2;
		}
	    }
	    // System.out.println("non è una keyword:" + temp);
	    // is variable
	    if (Character.isDigit(text_line.charAt(i)))
		throw new SyntaxError("ERRORE: i nomi di variabili non ammettono numeri. manca uno spazio");
	    Token t = new Variable(temp.toString().trim());
	    myList.addToken(t);

	    // temp.delete(0, temp.length()); // azzero il buff
	    // System.out.println("tempvale:" + temp);
	    // System.out.println("facciounsaltodi:" + increase_counter);
	    return temp.length() - 1;
	}

	else {
	    return 0;
	}
    }

    private int check_special_symbol(StringBuffer text_line, int i) {

	if (text_line.charAt(i) == '(') {
	    Token t = new SpecialSymbol(SpecialSymbol.tag.OPEN_PARENTHESIS);
	    myList.addToken(t);
	    // temp.delete(0, temp.length()); // azzero il buff

	} else if (text_line.charAt(i) == ')') {
	    Token t = new SpecialSymbol(SpecialSymbol.tag.CLOSE_PARENTHESIS);
	    myList.addToken(t);
	    // temp.delete(0, temp.length()); // azzero il buff

	}
	return 0;
    }

    private int check_number(StringBuffer text_line, int i) {
	StringBuffer temp = new StringBuffer();
	// caso numero negativo
	if ((text_line.charAt(i) == '-') && (Character.isDigit(text_line.charAt(i + 1)))) {
	    temp.append(text_line.charAt(i)); // appendo il "-"
	    compute_number(temp, text_line, i + 1);
	    int increase_counter = temp.length() - 1;
	    Token t = new Constant(Long.parseLong(temp.toString()));
	    this.myList.addToken(t);
	    temp.delete(0, temp.length()); // azzero il buff
	    return increase_counter;
	}

	// caso numero positivo
	else if (Character.isDigit(text_line.charAt(i))) {
	    // System.out.println("sto facendo il check:" + text_line.charAt(i));
	    // System.out.println("temp vale:" + temp.toString());
	    compute_number(temp, text_line, i);
	    int increase_counter = temp.length() - 1;
	    Token t = new Constant(Long.parseLong(temp.toString()));
	    this.myList.addToken(t);
	    temp.delete(0, temp.length()); // azzero il buff
	    return increase_counter;
	}

	// errore
	else if (text_line.charAt(i) == '-' && isChar(text_line.charAt(i + 1))) {
	    // System.out.println();
	    throw new SyntaxError("(ERROR in parser: expected long number, got '-')");
	}
	return 0;
    }

    private void compute_number(StringBuffer temp, StringBuffer text_line, int i) {
	if ((i < text_line.length()) && (text_line.charAt(i) == '0' && Character.isDigit(text_line.charAt(i + 1)))) {
	    throw new SyntaxError("ERROR: la grammatica per i numeri non e' rispettata.");
	} else {

	    while ((i < text_line.length()) && Character.isDigit(text_line.charAt(i))) {
		temp.append(text_line.charAt(i));
		i++;
	    }
	}
    }

    private boolean isChar(char c) {
	if ((c >= 'a' && c <= 'z') || c >= 'A' && c <= 'Z')
	    return true;
	else
	    return false;
    }
}
