package src.battleship;

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