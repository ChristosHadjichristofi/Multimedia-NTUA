package battleship;

/**
 * this exception is thrown when a ship type is already been placed and user tries to add a second
 */
public class InvalidCountException extends Exception {

    private static final long serialVersionUID = 1956429801328644600L;

    public InvalidCountException(String e) {
        super(e);
    }

}
