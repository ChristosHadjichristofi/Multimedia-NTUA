package src.battleship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    
    static Player player = new Player("player");
    static Enemy enemy = new Enemy("enemy");

    public static void main(String[] args) throws IOException, InvalidCountException, OversizeException,
            OverlapTilesException, AdjacentTilesException, NecessaryFileException {

        String pathPlayer = "medialab/player_default.txt";
        String pathEnemy = "medialab/enemy_default.txt";
        
        try {
            readInput(player, pathPlayer);
        } catch (InvalidCountException | NecessaryFileException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        try {
            readInput(enemy, pathEnemy);
        } catch (InvalidCountException | NecessaryFileException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        playGame();

        gameEnded();
        
    }

    private static void gameEnded() {

        if (player.intactShips == 0) {
            System.out.println("You lost. All of your ships have been destroyed!");
            System.out.println("Your Points: " + player.points);
        }
        else if (enemy.intactShips == 0) {
            System.out.println("You won. All ships of the computer have been destroyed!");
            System.out.println("Your Points: " + player.points);
        }
        else {
            if (player.points > enemy.points) System.out.println("You won with " + (player.points - enemy.points) + " points difference!");
            else if (player.points < enemy.points) System.out.println("You lost with " + (enemy.points - player.points) + " points difference!");
            else System.out.println("It's a tie. Both of you gathered " + player.points + " points!");
        }

    }

    private static void playGame() {

        Scanner scanner = new Scanner(System.in);
        Pair<Integer, Integer> shootCoords;
        Boolean move = false;
        int x, y;
        String difficulty;

        do {
            
            System.out.print("Select valid difficulty(Easy, Medium, Hard, Impossible): ");
            difficulty = scanner.nextLine();

        } while (!difficulty.equals("Easy") && !difficulty.equals("Medium") && !difficulty.equals("Hard") && !difficulty.equals("Impossible"));

        move = coinFlip();
        if (move) System.out.println("You're doing the first move!");
        else System.out.println("The computer is doing the first move!");

        while (player.intactShips > 0 && player.availableShoots > 0 && enemy.intactShips > 0 && enemy.availableShoots > 0){

            if (move) {
                
                do {
                    System.out.print("Please enter (x,y) coordinates to shoot: ");
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    while(x < 0 || x >= 10 || y < 0 || y >= 10){
                        System.out.print("Please give valid (x,y) coordinates: ");
                        x = scanner.nextInt();
                        y = scanner.nextInt();
                    }
                    shootCoords = new Pair<Integer, Integer>(x, y);
                } while(!player.shoot(player, enemy, shootCoords.getX(), shootCoords.getY()));

                move = false;
                player.availableShoots--;
                System.out.println("");
                player.grid.printGrids(player, enemy);
            }
            else {
                switch(difficulty) {
                    case "Medium":
                      enemy.moveMedium(player, enemy);
                      break;
                    case "Hard":
                      enemy.moveHard(player, enemy);
                      break;
                    case "Impossible":
                      enemy.moveImpossible(player, enemy);
                      break;
                    default:
                      enemy.moveEasy(player, enemy);
                      break;
                  }
                
                move = true;
                enemy.availableShoots--;
            }
        }

        if (scanner != null) scanner.close();
    }

    private static boolean coinFlip() {
        // Math random returns a value from 0.0 to 1.0. If it is less than 0.5 then
        // player is playing first, else the computer is playing first.
        return (Math.random() < 0.5) ? true : false;
    }

    public static void readInput(Player p, String path) throws IOException, InvalidCountException, OversizeException,
            OverlapTilesException, AdjacentTilesException, NecessaryFileException {
        
        String line;
        int shipType, cordX, cordY, orientation;
        File input = new File(path);
        BufferedReader reader;
        

        try {

            reader = new BufferedReader(new FileReader(input));

            try {
            
                while ((line = reader.readLine()) != null) {
    
                    shipType = Character.getNumericValue(line.charAt(0));
                    cordX = Character.getNumericValue(line.charAt(2));
                    cordY = Character.getNumericValue(line.charAt(4));
                    orientation = Character.getNumericValue(line.charAt(6));
        
                    if (shipType == 1){
                        if (p.carrier == null) {
                            p.carrier = new Carrier();
                            try {
                                p.carrier.addShip(p, shipType, cordX, cordY, orientation);
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
                                p.battleship.addShip(p, shipType, cordX, cordY, orientation);
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
                                p.cruiser.addShip(p, shipType, cordX, cordY, orientation);
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
                                p.submarine.addShip(p, shipType, cordX, cordY, orientation);
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
                                p.destroyer.addShip(p, shipType, cordX, cordY, orientation);
                            } catch (OversizeException | OverlapTilesException | AdjacentTilesException e) {
                                System.out.println(e.getMessage());
                            }                
                        }
                        else throw new InvalidCountException("This ship type (" + shipType + ") was already inserted in the grid!");
                    }
                }

            } finally {
                if (reader != null) reader.close();
            }

        } catch (FileNotFoundException e) {
            throw new NecessaryFileException("NecessaryFileException: necessary file (" + input + ") was not present." );
        }
    }
}

