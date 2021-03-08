package battleship;

import java.util.ArrayList;

/**
 * player class, has Grid, one ship of each type, name, available shoots, intact ships
 * successLastShot (is true if last shot hit ship, false if it did not)
 * failLastShot (is true if last shot failed, true if it did not)
 * successfulShoots (how many shots where successful (hit ship) till now)
 */
public class Player {
    
    Grid grid;
    Carrier carrier;
    Battleship battleship;
    Cruiser cruiser;
    Submarine submarine;
    Destroyer destroyer;
    String name;
    int availableShoots;
    int intactShips;
    int points;
    int successLastShot;
    int failLastShot;
    int successfulShoots;

    public Player(String name){
        
        grid = new Grid();
        this.name = name;
        availableShoots = 40;
        intactShips = 5;
        points = 0;
        successfulShoots = 0;
    }

    /**
     * method to shoot opponent. gets player (which might be the user or the bot), the enemy (which might be the user or the bot)
     * and x,y coordinates that is the target for the shoot. The logic here is that the numbering that was said in Grid.java
     * is used.
     * @param p player object that shoots
     * @param e enemy object that takes the shot
     * @param x x coordinate
     * @param y y coordinate
     * @return true if ship was hit, false if sea was hit
     */
  //
	public boolean shoot(Player p, Player e, int x, int y) {

        int valAtGrid = e.grid.shipTypeAtPos(x, y, e);

        switch(valAtGrid) {
            case 0:
              e.grid.grid[x][y] = 7;
              failLastShot++;
              return true;
            
            case 1:
              e.shipShotAt(e, x, y, e.carrier.positions);
              p.points += e.carrier.shotPoints;
              p.successLastShot++;
              p.successfulShoots++;
              if (e.carrier.checkStatus().equals("sank")){
                p.points += e.carrier.bonusPoints;
                e.intactShips--;
              }
              return true;
            
            case 2:
              e.shipShotAt(e, x, y, e.battleship.positions);
              p.points += e.battleship.shotPoints;
              p.successLastShot++;
              p.successfulShoots++;
              if (e.battleship.checkStatus().equals("sank")){
                p.points += e.battleship.bonusPoints;
                e.intactShips--;
              }
              return true;

            case 3:
              e.shipShotAt(e, x, y, e.cruiser.positions);
              p.points += e.cruiser.shotPoints;
              p.successLastShot++;
              p.successfulShoots++;
              if (e.cruiser.checkStatus().equals("sank")){
                p.points += e.cruiser.bonusPoints;
                e.intactShips--;
              }
              return true;
            
            case 4:
              e.shipShotAt(e, x, y, e.submarine.positions);
              p.points += e.submarine.shotPoints;
              p.successLastShot++;
              p.successfulShoots++;
              if (e.submarine.checkStatus().equals("sank")){
                p.points += e.submarine.bonusPoints;
                e.intactShips--;
              }
              return true;
            
            case 5:
              e.shipShotAt(e, x, y, e.destroyer.positions);
              p.points += e.destroyer.shotPoints;
              p.successLastShot++;
              p.successfulShoots++;
              if (e.destroyer.checkStatus().equals("sank")){
                p.points += e.destroyer.bonusPoints;
                e.intactShips--;
              }
              return true;

            default:
              return false;
          }
	}

    /**
     * method to replace value in grid so as the ship is shot. Code that means a ship is shot is 6.
     * so place to x,y number 6 and remove that x,y from that specific's ship type that tile
     *
     * @param e enemy object
     * @param x x coordinate
     * @param y y coordinate
     * @param positions array of that specific ship, in order to remove x,y from its positions as it has been hit
     */
    private void shipShotAt(Player e, int x, int y, ArrayList<Pair<Integer, Integer>> positions) {
        e.grid.grid[x][y] = 6;
        int index = 0;
        for (Pair<Integer, Integer> pos : positions) {
            if (pos.getX() == x && pos.getY() == y){
                positions.remove(index);
                break;
            }
            index++;
        }
    }
}
