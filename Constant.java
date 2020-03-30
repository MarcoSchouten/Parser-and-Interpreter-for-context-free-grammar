
public class Constant implements Token {
    /**
     * member variables
     */
    private Long value;

    /**
     * methods
     */
    public Constant(Long value) {
	this.value = value;
    }

    public String getValue() {
	return this.value.toString();
    }

    public void setValue(long v) {
	value = v;
    }

    public String getType() {
	return "constant";
    }
}