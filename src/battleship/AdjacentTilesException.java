package battleship;

// this exception is thrown when a ship has other ships in horizontal/vertical contact with it
public class AdjacentTilesException extends Exception {

    private static final long serialVersionUID = 7786946694939714589L;

    public AdjacentTilesException(String e) {
        super(e);
    }
}
