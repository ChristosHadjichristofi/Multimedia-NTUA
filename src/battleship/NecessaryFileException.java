package battleship;

/**
 * this exception is thrown when an input file is missing
 */
public class NecessaryFileException extends Exception {

    private static final long serialVersionUID = -8342681935846086779L;

    public NecessaryFileException(String e) {
        super(e);
    }

}
