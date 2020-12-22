package src.battleship;

// this exception is thrown when a ship gets out of grid bounds
public class OversizeException extends Exception {

    private static final long serialVersionUID = 1L;

    public OversizeException(String e) {
        super(e);
    }
    
}
