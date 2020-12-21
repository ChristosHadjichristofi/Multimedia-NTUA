package src;

import java.util.ArrayList;

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
    ArrayList<Triplet<Integer, Integer, Integer>> nextShots;

    public Player(String name){
        
        grid = new Grid();
        this.name = name;
        availableShoots = 40;
        intactShips = 5;
        points = 0;
    }

	public boolean shoot(Player p, Player e, int x, int y) {
        
        int valAtGrid = e.grid.shipTypeAtPos(x, y, e);

        switch(valAtGrid) {
            case 0:
              if (p.name == "player") System.out.println("Your shot went in the sea!");
              if (p.name == "enemy") System.out.println("Enemy's shot went in the sea!");
              e.grid.grid[x][y] = 7;
              return true;
            
            case 1:
              e.shipShotAt(p, e, x, y, e.carrier.positions);
              if (p.name == "player") System.out.println("You shot a ship of type 1! This ship has " + e.carrier.positions.size() + " parts left!");
              if (p.name == "enemy") System.out.println("Enemy shot your ship of type 1! This ship has " + e.carrier.positions.size() + " parts left!");
              p.points += e.carrier.shotPoints;
              p.successLastShot++;
              if (e.carrier.checkStatus() == "sank"){
                if (p.name == "player") System.out.println("Congrats! You sank enemy's ship of type 1!");
                if (p.name == "enemy") System.out.println("Oh snap! Enemy sank your ship of type 1!");
                p.points += e.carrier.bonusPoints;
                e.intactShips--;
              }                
              return true;
            
            case 2:
              e.shipShotAt(p, e, x, y, e.battleship.positions);
              if (p.name == "player") System.out.println("You shot a ship of type 2! This ship has " + e.battleship.positions.size() + " parts left!");
              if (p.name == "enemy") System.out.println("Enemy shot your ship of type 2! This ship has " + e.battleship.positions.size() + " parts left!");
              p.points += e.battleship.shotPoints;
              p.successLastShot++;
              if (e.battleship.checkStatus() == "sank"){
                if (p.name == "player") System.out.println("Congrats! You sank enemy's ship of type 2!");
                if (p.name == "enemy") System.out.println("Oh snap! Enemy sank your ship of type 2!");
                p.points += e.battleship.bonusPoints;
                e.intactShips--;
              } 
              return true;
            
            case 3:
              e.shipShotAt(p, e, x, y, e.cruiser.positions);
              if (p.name == "player") System.out.println("You shot a ship of type 3! This ship has " + e.cruiser.positions.size() + " parts left!");
              if (p.name == "enemy") System.out.println("Enemy shot your ship of type 3! This ship has " + e.cruiser.positions.size() + " parts left!");
              p.points += e.cruiser.shotPoints;
              p.successLastShot++;
              if (e.cruiser.checkStatus() == "sank"){
                if (p.name == "player") System.out.println("Congrats! You sank enemy's ship of type 3!");
                if (p.name == "enemy") System.out.println("Oh snap! Enemy sank your ship of type 3!");
                p.points += e.cruiser.bonusPoints;
                e.intactShips--;
              } 
              return true;
            
            case 4:
              e.shipShotAt(p, e, x, y, e.submarine.positions);
              if (p.name == "player") System.out.println("You shot a ship of type 4! This ship has " + e.submarine.positions.size() + " parts left!");
              if (p.name == "enemy") System.out.println("Enemy shot your ship of type 4! This ship has " + e.submarine.positions.size() + " parts left!");
              p.points += e.submarine.shotPoints;
              p.successLastShot++;
              if (e.submarine.checkStatus() == "sank"){
                if (p.name == "player") System.out.println("Congrats! You sank enemy's ship of type 4!");
                if (p.name == "enemy") System.out.println("Oh snap! Enemy sank your ship of type 4!");
                p.points += e.submarine.bonusPoints;
                e.intactShips--;
              } 
              return true;
            
            case 5:
              e.shipShotAt(p, e, x, y, e.destroyer.positions);
              if (p.name == "player") System.out.println("You shot a ship of type 5! This ship has " + e.destroyer.positions.size() + " parts left!");
              if (p.name == "enemy") System.out.println("Enemy shot your ship of type 5! This ship has " + e.submarine.positions.size() + " parts left!");
              p.points += e.destroyer.shotPoints;
              p.successLastShot++;
              if (e.destroyer.checkStatus() == "sank"){
                if (p.name == "player") System.out.println("Congrats! You sank enemy's ship of type 5!");
                if (p.name == "enemy") System.out.println("Oh snap! Enemy sank your ship of type 5!");
                p.points += e.destroyer.bonusPoints;
                e.intactShips--;
              } 
              return true;
            
            case 6:
              if (p.name == "player") System.out.println("You already shot at this position! It's a part of a ship that's hit!");
              return false;
            
            default:
              if (p.name == "player") System.out.println("You already shot at this position! It's sea!");
              return false;
          }
	}

    private void shipShotAt(Player p, Player e, int x, int y, ArrayList<Pair<Integer, Integer>> positions) {
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
