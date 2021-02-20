package battleship;

// this exception is thrown when user tries to tile/tiles that an other ship is already deployed
public class OverlapTilesException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public OverlapTilesException(String e) {
        super(e);
    }
    
}
