package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    
    static Player player = new Player();
    static Enemy enemy = new Enemy();

    public static void main(String[] args) throws IOException, InvalidCountException, OversizeException,
            OverlapTilesException, AdjacentTilesException {

        String pathPlayer = "medialab/player_default.txt";
        String pathEnemy = "medialab/enemy_default.txt";
        Boolean firstMove = false;
        try {
            readInput(player, pathPlayer);
        } catch (InvalidCountException e) {
            System.out.println(e.getMessage());
        }

        try {
            readInput(enemy, pathEnemy);
        } catch (InvalidCountException e) {
            System.out.println(e.getMessage());
        }

        
        firstMove = coinFlip();
        if (firstMove) System.out.println("You're doing the first move!");
        else System.out.println("The computer is doing the first move!");
        
    }

    private static boolean coinFlip() {
        // Math random returns a value from 0.0 to 1.0. If it is less than 0.5 then
        // player is playing first, else the computer is playing first.
        return (Math.random() < 0.5) ? true : false;
    }

    public static void readInput(Player p, String path) throws IOException, InvalidCountException, OversizeException,
            OverlapTilesException, AdjacentTilesException {
        
        String line;
        int shipType, cordX, cordY, orientation;
        File input = new File(path);
        
        BufferedReader reader = new BufferedReader(new FileReader(input));

        try {
            while ((line = reader.readLine()) != null){

                shipType = Character.getNumericValue(line.charAt(0));
                cordX = Character.getNumericValue(line.charAt(2));
                cordY = Character.getNumericValue(line.charAt(4));
                orientation = Character.getNumericValue(line.charAt(6));
    
                if (shipType == 1){
                    if (p.carrier == null) {
                        p.carrier = new Carrier();
                        try {
                            p.addShip(shipType, cordX, cordY, orientation, p.carrier);
                        } catch (OversizeException | OverlapTilesException | AdjacentTilesException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    else throw new InvalidCountException("This ship type (" + shipType + ") was already inserted in the grid!");
                }
                if (shipType == 2){
                    if (p.battleship == null) {
                        p.battleship = new Battleship();
                        try {
                            p.addShip(shipType, cordX, cordY, orientation, p.battleship);
                        } catch (OversizeException | OverlapTilesException | AdjacentTilesException e) {
                            System.out.println(e.getMessage());
                        } 
                    }
                    else throw new InvalidCountException("This ship type (" + shipType + ") was already inserted in the grid!");
                }
                if (shipType == 3){
                    if (p.cruiser == null) {
                        p.cruiser = new Cruiser();
                        try {
                            p.addShip(shipType, cordX, cordY, orientation, p.cruiser);
                        } catch (OversizeException | OverlapTilesException | AdjacentTilesException e) {
                            System.out.println(e.getMessage());
                        } 
                    }
                    else throw new InvalidCountException("This ship type (" + shipType + ") was already inserted in the grid!");
                }
                if (shipType == 4){
                    if (p.submarine == null) {
                        p.submarine = new Submarine();
                        try {
                            p.addShip(shipType, cordX, cordY, orientation, p.submarine);
                        } catch (OversizeException | OverlapTilesException | AdjacentTilesException e) {
                            System.out.println(e.getMessage());
                        } 
                    }
                    else throw new InvalidCountException("This ship type (" + shipType + ") was already inserted in the grid!");
                }
                if (shipType == 5){
                    if (p.destroyer == null) {
                        p.destroyer = new Destroyer();
                        try {
                            p.addShip(shipType, cordX, cordY, orientation, p.destroyer);
                        } catch (OversizeException | OverlapTilesException | AdjacentTilesException e) {
                            System.out.println(e.getMessage());
                        }                
                    }
                    else throw new InvalidCountException("This ship type (" + shipType + ") was already inserted in the grid!");
                }
            } 
        } 
        catch(FileNotFoundException e){}
        catch(IOException e) {}
        finally {
            try {
            if (reader != null) reader.close();
            } catch (IOException e) {
            e.printStackTrace();
            }   
        }

    }
}

