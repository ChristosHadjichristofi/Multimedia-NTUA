package battleship;

import javafx.scene.control.Alert;

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


    // method that deploys the ships of both player and enemy. uses the readInput method to complete this task
    private static void deployShips(String pathPlayer, String pathEnemy)
            throws IOException, OversizeException, OverlapTilesException, AdjacentTilesException, InvalidCountException, NecessaryFileException {

        try {
            readInput(player, pathPlayer);
            allPlayerShips.addAll(player.carrier.positionsAndType);
            allPlayerShips.addAll(player.cruiser.positionsAndType);
            allPlayerShips.addAll(player.submarine.positionsAndType);
            allPlayerShips.addAll(player.destroyer.positionsAndType);
            allPlayerShips.addAll(player.battleship.positionsAndType);
        } catch (InvalidCountException e) {
            throw(e);
        } catch (NecessaryFileException e) {
            throw(e);
        }

        try {
            readInput(enemy, pathEnemy);
            allEnemyShips.addAll(enemy.carrier.positionsAndType);
            allEnemyShips.addAll(enemy.cruiser.positionsAndType);
            allEnemyShips.addAll(enemy.submarine.positionsAndType);
            allEnemyShips.addAll(enemy.destroyer.positionsAndType);
            allEnemyShips.addAll(enemy.battleship.positionsAndType);
        } catch (InvalidCountException e) {
            throw(e);
        } catch (NecessaryFileException e) {
            throw(e);
        }

	}

    // method to show who won and the points
	public static String gameEnded() {

        String text = "";

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

    public static boolean playerTurn(int x, int y) {
        if (!player.shoot(player, enemy, x, y)) return false;
        player.availableShoots--;
        return true;
    }

    public static Triplet<Integer, Integer, Integer> enemyTurn(int d) {
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

    public static boolean coinFlip() {
        // Math random returns a value from 0.0 to 1.0. If it is less than 0.5 then
        // player is playing first, else the computer is playing first.
        return (Math.random() < 0.5) ? true : false;
    }

    // method that completes the reading and deployment of ships
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

