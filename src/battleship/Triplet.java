package battleship;

/**
 * triplet class in order to create objects that hold 3 values.
 * this class was used to create objects to hold the information for shooting coordinates
 * in order to make the bot(pc) to shoot with some logic (without cheating)
 * @param <T1> any variable type
 * @param <T2> any variable type
 * @param <T3> any variable type
 */
public class Triplet<T1, T2, T3> {

    private final T1 x;
    private final T2 y;
    private final T3 orientation;

    public Triplet(T1 x, T2 y, T3 orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public T1 getX() { return x; }
    public T2 getY() { return y; }
    public T3 getOrientation() { return orientation; }
}