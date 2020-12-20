package src;

import java.util.ArrayList;

public class Player {
    
    Grid playerGrid;
    Carrier carrier;
    Battleship battleship;
    Cruiser cruiser;
    Submarine submarine;
    Destroyer destroyer;
    int availableShoots;

    public Player(){
        
        playerGrid = new Grid();
        availableShoots = 40;
    }

    public void addShip(int shipType, int cordX, int cordY, int orientation, Ship s) throws OversizeException, OverlapTilesException,
            AdjacentTilesException {

        if (orientation == 1){
            for (int j = cordY; j < cordY + s.size; j++) {
                if (cordX < 0 || cordX >= 10 || j < 0 || j >= 10) throw new OversizeException("Cannot place ship of type " + s.type + ".It's not inside the grid!");
                else{
                    if (playerGrid.grid[cordX][j] != 0) throw new OverlapTilesException("Cannot place ship of type " + s.type + " because there is already ship of type " + playerGrid.grid[cordX][j] + ".");
                    else{
                       if (!validAdjacentTiles(cordX, j, s.type)) throw new AdjacentTilesException("Cannot place ship of type " + s.type + " because there is a ship in horizontal/vertical contact with it!");
                       else{
                           playerGrid.grid[cordX][j] = shipType;
                           s.positions.add(new Pair<Integer,Integer>(cordX,j));
                       }
                    }
                }
            }
        }   
        else {
            for (int i = cordX; i < cordX + s.size; i++) {
                if (cordY < 0 || cordY >= 10 || i < 0 || i >= 10) throw new OversizeException("Cannot place ship of type " + s.type + ".It's not inside the grid!");
                else{
                    if (playerGrid.grid[i][cordY] != 0) throw new OverlapTilesException("Cannot place ship of type " + s.type + " because there is already ship of type " + playerGrid.grid[i][cordY] + ".");
                    else{
                        if (!validAdjacentTiles(i, cordY, s.type)) throw new AdjacentTilesException("Cannot place ship of type " + s.type + " because there is a ship in horizontal/vertical contact with it!");
                        playerGrid.grid[i][cordY] = shipType;
                        s.positions.add(new Pair<Integer,Integer>(i,cordY));
                    }
                }
            }
        }
    }

    private boolean validAdjacentTiles(int x, int y, int type) {
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
                if (playerGrid.grid[cordX][cordY] != 0 && playerGrid.grid[cordX][cordY] != type) return false;
                else result.remove(i);
            }
        }
        return true;
    }
}
