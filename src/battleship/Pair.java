package src.battleship;

public class Pair<T1, T2> {

    private final T1 x;
    private final T2 y;

    public Pair(T1 x, T2 y) {
        this.x = x;
        this.y = y;
    }

    public T1 getX() { return x; }
    public T2 getY() { return y; }
}