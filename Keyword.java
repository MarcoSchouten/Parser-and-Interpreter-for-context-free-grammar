
public class Keyword implements Token {
    enum KeywordTag {
	GET, SET, ADD, SUB, MUL, DIV
    }

    /**
     * member variables
     */
    private KeywordTag value;

    /**
     * constructor
     * 
     * @param value
     */
    public Keyword(KeywordTag value) {
	this.value = value;
    }

    /**
     * method from interface
     */
    public String getValue() {
	return this.value.toString();
    }

    public String getType() {

	return "keyword";
    }
}
