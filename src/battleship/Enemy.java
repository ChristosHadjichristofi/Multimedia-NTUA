package src.battleship;

import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Player {

    ArrayList<Triplet<Integer, Integer, Integer>> nextShots;
    boolean foundShip = false;
    protected int failLimit = 3;

    public Enemy(String name) {
        super(name);
    }

    // bot's "logic" if player chooses to play against easy
    // bot is doing random shots without any logic
    public void moveEasy(Player p, Enemy e) {

        final int MAX = 10;
        final int MIN = 0;
        Random x, y;
        Pair<Integer, Integer> shootCoords;
        do {
            x = new Random();
            y = new Random();
            shootCoords = new Pair<Integer, Integer>(x.nextInt(MAX - MIN) + MIN, y.nextInt(MAX - MIN) + MIN);
        } while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY()));

    }

    // bot's logic if player chooses to play against medium bot is doing random shots, 
    // until it finds a part of a ship when bot finds a part of a ship, collects all valid 
    // adjacent tiles around the tile that player's ship was shot. On every other round bot 
    // gets one of those adjacent tiles, until it finds the second part of the ship. When the 
    // second part of the ship is found, then bot will know the orientation of that ship and 
    // will only shoot at that direction until player's ship is sank. When is sank bot will restart this logic.
    public void moveMedium(Player p, Enemy e) {

        final int MAX = 10;
        final int MIN = 0;
        Random x, y;
        Triplet<Integer, Integer, Integer> shootCoords;

        if (e.availableShoots == 40 || e.successLastShot == 0) {

            do {
                x = new Random();
                y = new Random();
                shootCoords = new Triplet<Integer, Integer, Integer>(x.nextInt(MAX - MIN) + MIN, y.nextInt(MAX - MIN) + MIN, 0);
            } while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY()));
            
            if (e.successLastShot > 0)
                e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());

        }

        else if (e.successLastShot == 1 && e.nextShots.size() > 0) {
            
            shootCoords = e.nextShots.remove(0);
            while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY())){

                if (e.nextShots.size() == 0){
                    e.successLastShot = 0;
                    break;
                }

                shootCoords = e.nextShots.remove(0);

            }

            if (e.successLastShot > 1) {
                ArrayList<Triplet<Integer, Integer, Integer>> previousShots = e.nextShots;
                
                e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());

                for (Triplet<Integer, Integer, Integer> prevShot : previousShots) {
                    if (prevShot.getX() == shootCoords.getX() || prevShot.getY() == shootCoords.getY()){
                        e.nextShots.add(prevShot);
                        break;
                    }
                }

                for (Triplet<Integer, Integer, Integer> i : e.nextShots) {
                    System.out.print( "(" + i.getX() + ", " + i.getY() +") ");
                }

            }
        }

        else if (e.successLastShot > 1 && e.nextShots.size() > 0) {
            int tempSuccessShots = e.successLastShot;

            shootCoords = e.nextShots.remove(0);
            while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY())){

                if (e.nextShots.size() == 0){
                    e.successLastShot = 0;
                    break;
                }

                shootCoords = e.nextShots.remove(0);

            }

            if (successLastShot > tempSuccessShots) {

                ArrayList<Triplet<Integer, Integer, Integer>> previousShots = e.nextShots;
                e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());
                for (Triplet<Integer, Integer, Integer> prevShot : previousShots)
                    e.nextShots.add(prevShot);
                
                for (Triplet<Integer, Integer, Integer> i : e.nextShots) {
                    System.out.print( "(" + i.getX() + ", " + i.getY() +") ");
                }

            }
            else
                e.successLastShot = 0;
        }
        // means pc shot a ship on last play but had no valid adjacent tiles, so 
        // it needs to attack randomly
        else {
            
            e.successLastShot = 0;

            do {
                x = new Random();
                y = new Random();
                shootCoords = new Triplet<Integer, Integer, Integer>(x.nextInt(MAX - MIN) + MIN, y.nextInt(MAX - MIN) + MIN, 0);
            } while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY()));
            
            if (e.successLastShot > 0)
                e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());
        }

    }

    // bot's logic if the player chooses to play against hard. Bot has a fail limit 
    // that is defined by the programmer at first bot starts shooting randomly. If it 
    // finds a part of a ship then bot follows the same tactic as moveMedium. What is 
    // changed from moveMedium is that when bot reaches that fail limit, it cheats and 
    // finds a random tile that has a part of a ship of the player that is not shot, 
    // and uses it for the next shot.
    public void moveHard(Player p, Enemy e) {

        final int MAX = 10;
        final int MIN = 0;
        Random x, y, randIndex;
        Pair<Integer, Integer> randElem;
        Triplet<Integer, Integer, Integer> shootCoords;

        // if not found a ship already
        if (!e.foundShip) {
            
            // if pc didn't reach random shoot limit without finding a ship
            if (e.failLastShot <= e.failLimit) {
                
                // find a random x,y that is valid and pc didn't shoot there
                do {
                    x = new Random();
                    y = new Random();
                    shootCoords = new Triplet<Integer, Integer, Integer>(x.nextInt(MAX - MIN) + MIN, y.nextInt(MAX - MIN) + MIN, 0);
                } while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY()));

                // if pc randomly found a ship, get all adjacent tiles that are valid to be next target
                // set found ship variable to true
                if (e.successLastShot > 0) {
                    e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());
                    e.foundShip = true;
                }
                
            }
            
            // if exceeded the random shoot limit without finding a ship
            else {

                ArrayList<Pair<Integer, Integer>> targets = new ArrayList<Pair<Integer, Integer>>();

                // get all ships that are still intact (their parts that are not shot yet) and place them in a new arraylist
                targets.addAll(p.carrier.positions);
                targets.addAll(p.cruiser.positions);
                targets.addAll(p.submarine.positions);
                targets.addAll(p.destroyer.positions);
                targets.addAll(p.battleship.positions);

                // randomly get one target
                randIndex = new Random();
                randElem = targets.get(randIndex.nextInt(targets.size()));

                // prepare shoot coords and set orientation to unkown (which is 0) and excecute the shoot
                shootCoords = new Triplet<Integer, Integer, Integer>(randElem.getX(), randElem.getY(), 0);
                shoot(e, p, shootCoords.getX(), shootCoords.getY());

                // get valid adjacent tiles to shoot next
                e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());

                // reduce failLimit so it becomes harder with time, make failLastShot eq to 0 and set foundShip to true
                e.failLimit--;
                e.successLastShot = 1;
                e.failLastShot = 0;
                e.foundShip = true;

            }
        }
        // ship is found
        else {

            // in case pc found only one piece of ship
            if (e.successLastShot == 1 && e.nextShots.size() > 0){
                
                // get the first of valid adjacent tiles and complete the shot
                shootCoords = e.nextShots.remove(0);
                while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY())){
    
                    if (e.nextShots.size() == 0){
                        e.successLastShot = 0;
                        e.failLastShot = 0;
                        e.foundShip = false;
                        break;
                    }
    
                    shootCoords = e.nextShots.remove(0);
    
                }
                
                // after the shot, if it was successful then get the tiles that are valid. This time will only get the 
                // right orientation of the ship, and so it will only succeed on next shot (unless ship doesn't have an other piece)
                if (e.successLastShot > 1){
                    ArrayList<Triplet<Integer, Integer, Integer>> previousShots = e.nextShots;
                    
                    e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());
                    
                    for (Triplet<Integer, Integer, Integer> prevShot : previousShots) {
                        if (prevShot.getX() == shootCoords.getX() || prevShot.getY() == shootCoords.getY()){
                            e.nextShots.add(prevShot);
                            break;
                        }
                    }

                    for (Triplet<Integer, Integer, Integer> i : e.nextShots) {
                        System.out.print( "(" + i.getX() + ", " + i.getY() +") ");
                    }
                }
            }

            // if more than one pieces of the ship found
            else if (e.successLastShot > 1 && e.nextShots.size() > 0){
                
                // keep the number of success shots till now
                int tempSuccessShots = e.successLastShot;
                
                // execute the shot
                shootCoords = e.nextShots.remove(0);
                while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY())){
    
                    if (e.nextShots.size() == 0){
                        e.successLastShot = 0;
                        e.failLastShot = 0;
                        e.foundShip = false;
                        break;
                    }
    
                    shootCoords = e.nextShots.remove(0);
    
                }
                
                // if this shot was successful too prepare for the next 
                if (e.successLastShot > tempSuccessShots) {
                    ArrayList<Triplet<Integer, Integer, Integer>> previousShots = e.nextShots;
                    
                    e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());
                    
                    for (Triplet<Integer, Integer, Integer> prevShot : previousShots)
                        e.nextShots.add(prevShot);
                    
                    for (Triplet<Integer, Integer, Integer> i : e.nextShots) {
                        System.out.print( "(" + i.getX() + ", " + i.getY() +") ");
                    }
                }
                // else it means that the ship is down.
                else {
                    e.successLastShot = 0;
                    e.failLastShot = 0;
                    e.foundShip = false;
                }
            }
            // else it means that bot previous round's shot was successful but had no valid adjacent tiles
            // so it needs to randomly shot
            else {

                e.successLastShot = 0;    
                e.failLastShot = 0;
                e.failLimit--;
                e.foundShip = false;

                do {
                    x = new Random();
                    y = new Random();
                    shootCoords = new Triplet<Integer, Integer, Integer>(x.nextInt(MAX - MIN) + MIN, y.nextInt(MAX - MIN) + MIN, 0);
                } while (!e.shoot(e, p, shootCoords.getX(), shootCoords.getY()));

                // if pc randomly found a ship, get all adjacent tiles that are valid to be next target
                // set found ship variable to true
                if (e.successLastShot > 0) {
                    e.nextShots = nextValidShoot(shootCoords.getX(), shootCoords.getY(), shootCoords.getOrientation());
                    e.foundShip = true;
                }
            }
        }
    }

    // bot's logic when player chooses to play against impossible. Now bot cheats :D
    // the only way to win is to guess without doing any mistakes and playing first :D
    public void moveImpossible(Player p, Player e) {

        Random randIndex;
        Pair<Integer, Integer> randElem;
        Triplet<Integer, Integer, Integer> shootCoords;
        ArrayList<Pair<Integer, Integer>> targets = new ArrayList<Pair<Integer, Integer>>();

        targets.addAll(p.carrier.positions);
        targets.addAll(p.cruiser.positions);
        targets.addAll(p.submarine.positions);
        targets.addAll(p.destroyer.positions);
        targets.addAll(p.battleship.positions);

        randIndex = new Random();
        randElem = targets.get(randIndex.nextInt(targets.size()));

        shootCoords = new Triplet<Integer, Integer, Integer>(randElem.getX(), randElem.getY(), 0);
        shoot(e, p, shootCoords.getX(), shootCoords.getY());

    }

    // method that finds next valid shoot. This method is used for moveMedium/moveHard in order to find the adjacent
    // tiles that might contain an other part of a ship.
    private ArrayList<Triplet<Integer, Integer, Integer>> nextValidShoot(Integer x, Integer y, Integer orientation) {
        int cordX, cordY;
     
        ArrayList<Triplet<Integer, Integer, Integer>> shots = new ArrayList<Triplet<Integer, Integer, Integer>>();

        if (orientation == 1 || orientation == 0){
            shots.add(new Triplet<Integer, Integer, Integer>(x, y - 1, 1)); // l
            shots.add(new Triplet<Integer, Integer, Integer>(x, y + 1, 1)); // r
        }
        if (orientation == 2 || orientation == 0){
            shots.add(new Triplet<Integer, Integer, Integer>(x - 1, y, 2)); // u
            shots.add(new Triplet<Integer, Integer, Integer>(x + 1, y, 2)); // d
        }

        for (int i = 0; i < shots.size(); i++) {
            Triplet<Integer, Integer, Integer> coordinates = shots.get(i);
            cordX = coordinates.getX();
            cordY = coordinates.getY();

            if (cordX < 0 || cordX >= 10 || cordY < 0 || cordY >= 10) shots.remove(i);
        }

        return shots;
    }
}
