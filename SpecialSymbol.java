
public class SpecialSymbol implements Token {
    enum tag {
	OPEN_PARENTHESIS, CLOSE_PARENTHESIS, END
    }

    /**
     * member variables
     */
    private tag value;

    /**
     * constructor
     * 
     * @param value
     */
    public SpecialSymbol(tag value) {
	this.value = value;
    }

    /**
     * method from interface
     */
    public String getValue() {
	return this.value.toString();
    }

    public String getType() {
	// TODO Auto-generated method stub
	return "specialsymbol";
    }

}
