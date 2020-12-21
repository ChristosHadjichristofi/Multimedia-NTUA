package src;

import java.util.ArrayList;

public class Ship {

    public int type;
    public int size;
    public int shotPoints;
    public int bonusPoints;
    public ArrayList<Pair<Integer,Integer>> positions;

    public Ship(int type, int size, int shotPoints, int bonusPoints){
        
        this.type = type;
        this.size = size;
        this.shotPoints = shotPoints;
        this.bonusPoints = bonusPoints;
        positions = new ArrayList<Pair<Integer,Integer>>();
    }

    public String checkStatus() {
        if (positions.size() == size){
            return "intact";
        }
        else if (positions.size() == 0){
            return "sank";
        }
        return "hit";
    }

    public void addShip(Player p, int shipType, int cordX, int cordY, int orientation) throws OversizeException, OverlapTilesException,
            AdjacentTilesException {

        if (orientation == 1){
            for (int j = cordY; j < cordY + size; j++) {
                if (cordX < 0 || cordX >= 10 || j < 0 || j >= 10) throw new OversizeException("Cannot place ship of type " + type + ".It's not inside the grid!");
                else{
                    if (p.grid.grid[cordX][j] != 0) throw new OverlapTilesException("Cannot place ship of type " + type + " because there is already ship of type " + p.grid.grid[cordX][j] + ".");
                    else{
                       if (!validAdjacentTiles(p, cordX, j, type)) throw new AdjacentTilesException("Cannot place ship of type " + type + " because there is a ship in horizontal/vertical contact with it!");
                       else{
                            p.grid.grid[cordX][j] = shipType;
                            positions.add(new Pair<Integer,Integer>(cordX, j));
                       }
                    }
                }
            }
        }   
        else {
            for (int i = cordX; i < cordX + size; i++) {
                if (cordY < 0 || cordY >= 10 || i < 0 || i >= 10) throw new OversizeException("Cannot place ship of type " + type + ".It's not inside the grid!");
                else{
                    if (p.grid.grid[i][cordY] != 0) throw new OverlapTilesException("Cannot place ship of type " + type + " because there is already ship of type " + p.grid.grid[i][cordY] + ".");
                    else{
                        if (!validAdjacentTiles(p, i, cordY, type)) throw new AdjacentTilesException("Cannot place ship of type " + type + " because there is a ship in horizontal/vertical contact with it!");
                        p.grid.grid[i][cordY] = shipType;
                        positions.add(new Pair<Integer,Integer>(i, cordY));
                    }
                }
            }
        }
    }

    private boolean validAdjacentTiles(Player p, int x, int y, int t) {
        int cordX, cordY;

        ArrayList<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>();
        result.add(new Pair<Integer, Integer>(x - 1, y));
        result.add(new Pair<Integer, Integer>(x + 1, y));
        result.add(new Pair<Integer, Integer>(x, y - 1));
        result.add(new Pair<Integer, Integer>(x, y + 1));

        for (int i = 0; i < result.size(); i++) {
            Pair<Integer, Integer> coordinates = result.get(i);
            cordX = coordinates.getX();
            cordY = coordinates.getY();

            if (cordX < 0 || cordX >= 10 || cordY < 0 || cordY >= 10) result.remove(i);
            else {
                if (p.grid.grid[cordX][cordY] != 0 && p.grid.grid[cordX][cordY] != t) return false;
                else result.remove(i);
            }
        }
        return true;
    }

}
