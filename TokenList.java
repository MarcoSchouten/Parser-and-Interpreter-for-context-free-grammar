import java.util.ArrayList;

public class TokenList {
    ArrayList<Token> myArr;

    TokenList() {
	myArr = new ArrayList<Token>();

    }

    public void addToken(Token t) {
	myArr.add(t);
    }

    public void printList() {
	for (Token t : myArr) {
	    System.out.print(" " + t.getValue() + " ");
	}
    }

    public Token getToken(int index) {
	return myArr.get(index);
    }

    public boolean isEmpty() {
	return myArr.isEmpty();
    }

    public int getLength() {
	return myArr.size();
    }

    public void insertInto(int index, Token element) {
	if (index >= 0)
	    myArr.add(index, element);
    }

}
