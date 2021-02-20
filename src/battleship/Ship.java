package battleship;

import java.util.ArrayList;

public class Ship {

    public int type;
    public int size;
    public int shotPoints;
    public int bonusPoints;
    public ArrayList<Pair<Integer,Integer>> positions;
    public ArrayList<Pair<Integer, Pair<Integer,Integer>>> positionsAndType;


    // constructor of ship. Every ship has a type, size, shotPoints, 
    //bonusPoints, positions arraylist (the tiles that this ship owns in grid)
    public Ship(int type, int size, int shotPoints, int bonusPoints){
        
        this.type = type;
        this.size = size;
        this.shotPoints = shotPoints;
        this.bonusPoints = bonusPoints;
        positions = new ArrayList<Pair<Integer,Integer>>();
        positionsAndType = new ArrayList<Pair<Integer, Pair<Integer,Integer>>>();
    }

    // method which returns the status of a ship.
    // if positions arraylist has the same size as size then ship is intact
    // if is less than size then is hit
    // if is zero then is sank
    public String checkStatus() {
        if (positions.size() == size){
            return "intact";
        }
        else if (positions.size() == 0){
            return "sank";
        }
        return "hit";
    }

    // method which adds ship on player's/enemy's grid
    public void addShip(Player p, int shipType, int cordX, int cordY, int orientation) throws OversizeException, OverlapTilesException,
            AdjacentTilesException {
        
        // check for orientation so is placed right
        if (orientation == 1){
            for (int j = cordY; j < cordY + size; j++) {
                // check if ship gets out of bounds of the grid to throw OversizeException
                if (cordX < 0 || cordX >= 10 || j < 0 || j >= 10) throw new OversizeException("Cannot place ship of type " + type + ".It's not inside the grid!");
                else{
                    // check if a ship is already placed at a tile that the current ship is trying to deploy to throw OverlapTilesException
                    if (p.grid.grid[cordX][j] != 0) throw new OverlapTilesException("Cannot place ship of type " + type + " because there is already ship of type " + p.grid.grid[cordX][j] + ".");
                    else{
                        // check if a ship has an other ship in horizontal/vertical contact with it to throw AdjacentTilesException 
                        // (this is done with the help of validAdjacentTiles)
                        if (!validAdjacentTiles(p, cordX, j, type)) throw new AdjacentTilesException("Cannot place ship of type " + type + " because there is a ship in horizontal/vertical contact with it!");
                        // if all checks passed, place ship
                        else{
                            // place ship to player's grid and add it to the specific ship type's positions arraylist
                            p.grid.grid[cordX][j] = shipType;
                            positions.add(new Pair<Integer,Integer>(cordX, j));
                            positionsAndType.add(new Pair<>(shipType, new Pair<>(cordX, cordY)));
                        }
                    }
                }
            }
        }
        // same logic but for orientation == 2
        else {
            for (int i = cordX; i < cordX + size; i++) {
                if (cordY < 0 || cordY >= 10 || i < 0 || i >= 10) throw new OversizeException("Cannot place ship of type " + type + ".It's not inside the grid!");
                else{
                    if (p.grid.grid[i][cordY] != 0) throw new OverlapTilesException("Cannot place ship of type " + type + " because there is already ship of type " + p.grid.grid[i][cordY] + ".");
                    else{
                        if (!validAdjacentTiles(p, i, cordY, type)) throw new AdjacentTilesException("Cannot place ship of type " + type + " because there is a ship in horizontal/vertical contact with it!");
                        p.grid.grid[i][cordY] = shipType;
                        positions.add(new Pair<>(i, cordY));
                        positionsAndType.add(new Pair<>(shipType, new Pair<>(cordX, cordY)));
                    }
                }
            }
        }
    }

    // method which finds all adjacent tiles that are valid (not getting out of bounds) and returns
    // true if all tiles surrounding that x,y are ok (there isn't a ship exactly next to the other either horizontally or vertically)
    // or returns false if there is a ship exactly next to the other (horizontally/vertically)
    private boolean validAdjacentTiles(Player p, int x, int y, int t) {
        int cordX, cordY;

        ArrayList<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>();
        result.add(new Pair<>(x - 1, y));
        result.add(new Pair<>(x + 1, y));
        result.add(new Pair<>(x, y - 1));
        result.add(new Pair<>(x, y + 1));

        for (int i = 0; i < result.size(); i++) {
            Pair<Integer, Integer> coordinates = result.get(i);
            cordX = coordinates.getX();
            cordY = coordinates.getY();

            // checks if x,y not in bounds remove from possible adjacent tiles because its not valid
            if (cordX < 0 || cordX >= 10 || cordY < 0 || cordY >= 10) result.remove(i);
            // if is valid then
            else {
                // if that value of that x,y position at grid isn't equal to 0 or equal to ship's type return
                // false because it cannot be placed there as it has ship that is in horizontal/vertical contact
                // with it
                if (p.grid.grid[cordX][cordY] != 0 && p.grid.grid[cordX][cordY] != t) return false;
                // else remove it as that tile is fine 
                else result.remove(i);
            }
        }
        return true;
    }

}
