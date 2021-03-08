package battleship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PlayGame {

    // create player and enemy
    static Player player;
    static Enemy enemy;
    static ArrayList<Pair<Integer, Pair<Integer, Integer>>> allPlayerShips;
    static ArrayList<Pair<Integer, Pair<Integer, Integer>>> allEnemyShips;

    /**
     * method that starts the game, deploys ships and throws possible alerts using a object (object of main)
     * @param ScenarioID is the number/text of scenario name
     * @param a is the object of Main
     */
    public static void startGame(String ScenarioID, Main a) {

        player = new Player("player");
        enemy = new Enemy("enemy");
        allPlayerShips = new ArrayList<>();
        allEnemyShips = new ArrayList<>();

        String pathPlayer = "medialab/player_" + ScenarioID + ".txt";
        String pathEnemy = "medialab/enemy_" + ScenarioID + ".txt";

        try {
            deployShips(pathPlayer, pathEnemy);
        }
        catch (IOException e) {
            a.throwAlert("IOException", "Necessary file was not present!\nDue to error default scenarios will be loaded!");
        }
        catch (OversizeException e) {
            a.throwAlert("OversizeException", "Please make sure that no ships get out of bounds of the grid!\nDue to error default scenarios will be loaded!");
        }
        catch (OverlapTilesException e) {
            a.throwAlert("OverlapTilesException", "Please make sure that there is no overlap between ships!\nDue to error default scenarios will be loaded!");
        }
        catch (InvalidCountException e) {
            a.throwAlert("InvalidCountException", "Please make sure each type of ship only exists only once on the grid!\nDue to error default scenarios will be loaded!");
        }
        catch (NecessaryFileException e) {
            a.throwAlert("NecessaryFileException", "Necessary file/files you're trying to load does not exist!\nDue to error default scenarios will be loaded!");
        }
        catch (AdjacentTilesException e) {
            a.throwAlert("AdjacentTilesException", "Please make sure that around every ship, all cells are empty!\nDue to error default scenarios will be loaded!");
        }
    }


    /**
     * method that deploys the ships of both player and enemy. uses the readInput method to complete this task
     * @param pathPlayer string that holds where player's scenario is
     * @param pathEnemy string that holds where enemy's scenario is
     * @throws IOException exception for IO
     * @throws OversizeException exception for ship getting out of bounds
     * @throws OverlapTilesException exception for ship overlapping with an other ship
     * @throws AdjacentTilesException exception when every tile around a ship are not empty
     * @throws InvalidCountException exception when a ship type exists more than one time
     * @throws NecessaryFileException exception when necessary file is missing
     */
    private static void deployShips(String pathPlayer, String pathEnemy)
            throws IOException, OversizeException, OverlapTilesException, AdjacentTilesException, InvalidCountException, NecessaryFileException {

        try {
            readInput(player, pathPlayer);
            allPlayerShips.addAll(player.carrier.positionsAndType);
            allPlayerShips.addAll(player.cruiser.positionsAndType);
            allPlayerShips.addAll(player.submarine.positionsAndType);
            allPlayerShips.addAll(player.destroyer.positionsAndType);
            allPlayerShips.addAll(player.battleship.positionsAndType);
        } catch (InvalidCountException | NecessaryFileException e) {
            throw(e);
        }

        try {
            readInput(enemy, pathEnemy);
            allEnemyShips.addAll(enemy.carrier.positionsAndType);
            allEnemyShips.addAll(enemy.cruiser.positionsAndType);
            allEnemyShips.addAll(enemy.submarine.positionsAndType);
            allEnemyShips.addAll(enemy.destroyer.positionsAndType);
            allEnemyShips.addAll(enemy.battleship.positionsAndType);
        } catch (InvalidCountException | NecessaryFileException e) {
            throw(e);
        }

    }

    /**
     * method to show who won and the points
     * @return string to be shown in ui
     */
	public static String gameEnded() {

        String text;

        if (player.intactShips == 0)
            text = "You lost. All of your ships have been destroyed!\n Your Points: " + player.points;
        else if (enemy.intactShips == 0)
            text = "You won. All ships of the computer have been destroyed!\n Your Points: " + player.points;
        else {
            if (player.points > enemy.points) text = "You won with " + (player.points - enemy.points) + " points difference!";
            else if (player.points < enemy.points) text = "You lost with " + (enemy.points - player.points) + " points difference!";
            else text = "It's a tie. Both of you gathered " + player.points + " points!";
        }
        return text;
    }

    /**
     * @param x coord x where player shot
     * @param y coord y where player shot
     * @return true if shot completed, false if it did not so as player will shoot till his turn is completed
     */
    public boolean playerTurn(int x, int y) {
        if (!player.shoot(player, enemy, x, y)) return false;
        player.availableShoots--;
        return true;
    }

    /**
     * @param d difficulty level, so as bot plays with the right logic
     * @return enemyCoords: the shot that enemy made
     */
    public Triplet<Integer, Integer, Integer> enemyTurn(int d) {
        Triplet<Integer, Integer, Integer> enemyCoords;
        switch (d) {
            case 2:
                enemyCoords = enemy.moveMedium(player, enemy);
                break;
            case 3:
                enemyCoords = enemy.moveHard(player, enemy);
                break;
            case 4:
                enemyCoords = enemy.moveImpossible(player, enemy);
                break;
            default:
                enemyCoords = enemy.moveEasy(player, enemy);
                break;
        }
        enemy.availableShoots--;
        return enemyCoords;

    }

    /**
     * method to decide who plays first (enemy or player)
     * @return true or false
     */
    public static boolean coinFlip() {
        // Math random returns a value from 0.0 to 1.0. If it is less than 0.5 then
        // player is playing first, else the computer is playing first.
        return Math.random() < 0.5;
    }

    /**
     * method that completes the reading and deployment of ships
     * @param p the object of player (or enemy) that this file belong to
     * @param path string which shows where the input file is
     * @throws IOException exception for IO
     * @throws InvalidCountException exception when a ship type exists more than one time
     * @throws NecessaryFileException exception when necessary file is missing
     */
    public static void readInput(Player p, String path) throws IOException, InvalidCountException, NecessaryFileException {

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

            } finally { reader.close(); }

        } catch (FileNotFoundException e) {
            throw new NecessaryFileException("NecessaryFileException: necessary file (" + input + ") was not present." );
        }
    }
}

