package battleship;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.util.Duration;

import javax.swing.*;

public class Main extends Application implements Initializable {

    public boolean move;
    public boolean isOK;
    private boolean gameRunning = true;
    public Cell[][] playerCells;
    public Cell[][] enemyCells;
    public static int difficulty = 2;
    private static int hintUsed = 0;
    private static PlayGame playgame;
    private static String Scenario_ID;
    ArrayList<Pair<Integer, Integer>> playerShotsList;
    ArrayList<Pair<Integer, Integer>> enemyShotsList;

    @FXML
    private GridPane gridPlayer;
    @FXML
    private GridPane gridEnemy;
    @FXML
    private TextField iCoordinate;
    @FXML
    private TextField jCoordinate;
    @FXML
    private Label resultLabel;
    @FXML
    private Label enterCoords;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label playerScenario;
    @FXML
    private Label enemyScenario;
    @FXML
    private Button shootButton;
    @FXML
    private Label aliveShipsPlayer;
    @FXML
    private Label aliveShipsEnemy;
    @FXML
    private Label pointsPlayer;
    @FXML
    private Label pointsEnemy;
    @FXML
    private Label availableShootsPlayer;
    @FXML
    private Label availableShootsEnemy;
    @FXML
    private Label successfulShotsRatePlayer;
    @FXML
    private Label successfulShotsRateEnemy;

    public void setResultLabel(String s) { this.resultLabel.setText(s); }
    public void setEnterCoords(String s) { this.enterCoords.setText(s); }
    public void setDifficultyLabel(String s) { this.difficultyLabel.setText(s); }
    public void setPlayerScenarioLabel(String s) { this.playerScenario.setText(s); }
    public void setEnemyScenarioLabel(String s) { this.enemyScenario.setText(s); }
    private void setAliveShipsPlayer(int intactShips) { this.aliveShipsPlayer.setText("Ships Alive: " + intactShips); }
    private void setAliveShipsEnemy(int intactShips) { this.aliveShipsEnemy.setText("Ships Alive: " + intactShips); }
    private void setPointsPlayer(int points) { this.pointsPlayer.setText("Points: " + points); }
    private void setPointsEnemy(int points) { this.pointsEnemy.setText("Points: " + points); }
    private void setAvailableShootsPlayer(int shoots) { this.availableShootsPlayer.setText("Available Shoots: " + shoots); }
    private void setAvailableShootsEnemy(int shoots) { this.availableShootsEnemy.setText("Available Shoots: " + shoots); }
    private void setSuccessfulShotsRatePlayer(String rate) { this.successfulShotsRatePlayer.setText("Successful Shots Rate: " + rate); }
    private void setSuccessfulShotsRateEnemy(String rate) { this.successfulShotsRateEnemy.setText("Successful Shots Rate: " + rate); }

    public static class Cell extends Rectangle {
        public int i, j;

        /**
         * constructor for cell
         */
        public Cell() {
            super(40, 40);
            setStroke(Color.BLACK);
            setFill(Color.WHITE);
        }

        /**
         * constructor for cell
         * @param i position i of cell
         * @param j position j of cell
         */
        public Cell(int i, int j) {
            super(40, 40);
            this.i = i;
            this.j = j;
            setStroke(Color.BLACK);
            setFill(Color.WHITE);

        }

        public void isShip() { setFill(Color.YELLOWGREEN); }
        public void shipHit() { setFill(Color.RED); }
        public void seaHit() { setFill(Color.GRAY); }

        /**
         * method to update cell's color
         * @param cellVal value of cell
         */
        public void updateCell (int cellVal) {
            switch (cellVal) {
                default:
                    break;
                case 6:
                    shipHit();
                    break;
                case 7:
                    seaHit();
                    break;
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { initialize(); }

    /**
     * method to restart game
     */
    public void startAction(){
        initialize();
    }

    /**
     * method to load an other scenario
     */
    public void loadAction() {
        TextInputDialog lDialog = new TextInputDialog("Scenario_ID");
        lDialog.setHeaderText("Enter your game Scenario_ID (number):");
        lDialog.showAndWait();
        Scenario_ID = lDialog.getEditor().getText();
        initialize();
    }

    /**
     * method to set bots difficulty to easy (if player chose it)
     */
    //
    public void easy(){
        difficulty = 1;
        initialize();
        setDifficultyLabel("Mode: Easy");
    }

    /**
     * method to set bots difficulty to medium (if player chose it) - this is the default
     */
    public void medium(){
        difficulty = 2;
        initialize();
        setDifficultyLabel("Mode: Medium");
    }

    /**
     * method to set bots difficulty to hard (if player chose it)
     */
    public void hard(){
        difficulty = 3;
        initialize();
        setDifficultyLabel("Mode: Hard");
    }

    /**
     * method to set bots difficulty to impossible (if player chose it)
     */
    public void impossible(){
        difficulty = 4;
        initialize();
        setDifficultyLabel("Mode: Impossible");
    }

    /**
     * method to exit game
     */
    public void exitAction(){ Platform.exit(); }

    /**
     * method to show the status of enemy ships
     */
    public void enemyShipsAction() {
        String enemyShipsStatus = "";
        enemyShipsStatus += "Carrier: " + PlayGame.enemy.carrier.checkStatus() + "\n";
        enemyShipsStatus += "Battleship: " + PlayGame.enemy.battleship.checkStatus() + "\n";
        enemyShipsStatus += "Cruiser: " + PlayGame.enemy.cruiser.checkStatus() + "\n";
        enemyShipsStatus += "Submarine: " + PlayGame.enemy.submarine.checkStatus() + "\n";
        enemyShipsStatus += "Destroyer: " + PlayGame.enemy.destroyer.checkStatus() + "\n";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enemy Ships Condition");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(enemyShipsStatus);
        alert.showAndWait();

    }

    /**
     * method to show the 5 latest shots of player
     */
    public void playerShotsAction(){
        int lowBound, iCoord, jCoord, shotRes, shipTypeVal = 0;
        String shipType;
        StringBuilder playerShotsText = new StringBuilder();
        if (playerShotsList.size() < 5) lowBound = 0;
        else lowBound = playerShotsList.size() - 5;
        for (int i = playerShotsList.size() - 1; i >= lowBound; i --) {
            iCoord = playerShotsList.get(i).getX();
            jCoord = playerShotsList.get(i).getY();
            shotRes = PlayGame.enemy.grid.shipTypeAtPos(iCoord, jCoord, PlayGame.enemy);

            if (shotRes == 6) {
                for (Pair<Integer, Pair<Integer, Integer>> shipPos : PlayGame.allEnemyShips) {
                    if (shipPos.getY().getX() == iCoord && shipPos.getY().getY() == jCoord) {
                        shipTypeVal = shipPos.getX();
                        break;
                    }
                }
            }

            shipType = findShipType(shipTypeVal);

            playerShotsText.append("Position: (").append(iCoord).append(",").append(jCoord).append(") ")
                    .append((shotRes == 6) ? "Hit a Ship of type " + shipType + "\n" : "Missed\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Player Shots Info");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        if (playerShotsText.length() == 0) alert.setContentText("There are no previous moves made by you.");
        else alert.setContentText(String.valueOf(playerShotsText));
        alert.showAndWait();
    }

    /**
     * method to show the 5 latest shots of enemy
     */
    public void enemyShotsAction(){
        int lowBound, iCoord, jCoord, shotRes, shipTypeVal = 0;
        String shipType;
        StringBuilder enemyShotsText = new StringBuilder();
        if (enemyShotsList.size() < 5) lowBound = 0;
        else lowBound = enemyShotsList.size() - 5;
        for (int i = enemyShotsList.size() - 1; i >= lowBound; i --) {
            iCoord = enemyShotsList.get(i).getX();
            jCoord = enemyShotsList.get(i).getY();
            shotRes = PlayGame.player.grid.shipTypeAtPos(iCoord, jCoord, PlayGame.player);

            if (shotRes == 6) {
                for (Pair<Integer, Pair<Integer, Integer>> shipPos : PlayGame.allPlayerShips) {
                    if (shipPos.getY().getX() == iCoord && shipPos.getY().getY() == jCoord) {
                        shipTypeVal = shipPos.getX();
                        break;
                    }
                }
            }

            shipType = findShipType(shipTypeVal);

            enemyShotsText.append("Position: (").append(iCoord).append(",").append(jCoord).append(") ")
                    .append((shotRes == 6) ? "Hit a Ship of type " + shipType + "\n" : "Missed" + '\n');
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enemy Shots Info");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        if (enemyShotsText.length() == 0) alert.setContentText("There are no previous moves made by Enemy.");
        else alert.setContentText(String.valueOf(enemyShotsText));
        alert.showAndWait();
    }

    /**
     * method to show the instructions of the game
     */
    public void instrAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("General Info");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("You enter coordinates and press shoot.\n" +
                "You win/lose when all of your ships or enemy ships are down, or when you complete 40 shots.\n" +
                "You can set the difficulty of the bot from Application/Difficulty.\n" +
                "When you hit an enemy ship, the tile is marked red.\n" +
                "When you hit sea, the tile is marked blue.\n " +
                "Your ships are market with greenyellow at the start of the game.\n" +
                "Enemy ships are appeared at the end of the game with gold color.");
        alert.showAndWait();
    }

    /**
     * method to get a hint of where an enemy ship is
     */
    public void getHintAction(){
        if (!gameRunning) return;
        int repeat = JOptionPane.showConfirmDialog(null, "Would you like to get a hint? It will cost " +
                (hintUsed + 1) + " of your available shots!");
        if (repeat == 0) {
            hintUsed++;
            Pair<Integer, Integer> coords = PlayGame.enemy.help(PlayGame.enemy);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.005), evt -> enemyCells[coords.getX() + 1][coords.getY() + 1].setFill(Color.GREEN)),
                    new KeyFrame(Duration.seconds(1), evt -> enemyCells[coords.getX() + 1][coords.getY() + 1].setFill(Color.WHITE)));
            timeline.play();
            PlayGame.player.availableShoots -= hintUsed;
            if (PlayGame.player.availableShoots < 0) {
                PlayGame.player.availableShoots = 0;
                PlayGame.enemy.availableShoots = 0;
                setAvailableShootsPlayer(PlayGame.player.availableShoots);
                checkEndGame();
            }

            setAvailableShootsPlayer(PlayGame.player.availableShoots);
        }
    }

    /**
     * method to show what happens if you press get hint
     */
    public void aboutHintAction(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint Info");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("When you press 'get Hint', a tile on the opponent's grid will lit. \n" +
                "You can use it to make your next move... but be careful! \n" +
                "By using 'get Hint', your available shots decrease by the number of times you used the hint!\n" +
                "For example if you use the hint 2 times, you will lose 3 shots!\n" +
                "So...choose wisely.");
        alert.showAndWait();
    }

    /**
     * method to handle shoot button
     */
    public void shootAction () {
        int i, j;

        setResultLabel("");
        try {
            i = Integer.parseInt(iCoordinate.getText());
            j = Integer.parseInt(jCoordinate.getText());
            iCoordinate.setText("");
            jCoordinate.setText("");

            if (i < 0 || i > 9 || j < 0 || j > 9) {
                setResultLabel("Please set valid coordinates (0 <= i,j <= 9)!");
                return;
            }
        } catch (NumberFormatException e) {
            iCoordinate.setText("");
            jCoordinate.setText("");
            setResultLabel("Only numbers are allowed!");
            return;
        }
        if (!playgame.playerTurn(i, j)) {
            setResultLabel("This position was already hit");
            return;
        }
        playerShotsList.add(new Pair<>(i, j));
        enemyCells[i + 1][j + 1].updateCell(PlayGame.enemy.grid.grid[i][j]);
        makeMoveEnemy();

    }

    /**
     * method to find the ship type and return a string with the name of type of the ship
     *
     * @param shipTypeVal value of ship type in order to be recognised as Carrier/Battleship/Cruiser/Submarine/Destroyer
     * @return the ship type as string
     */
    private String findShipType(int shipTypeVal) {
        String shipType;

        if (shipTypeVal == 1) shipType = "Carrier";
        else if (shipTypeVal == 2) shipType = "Battleship";
        else if (shipTypeVal == 3) shipType = "Cruiser";
        else if (shipTypeVal == 4) shipType = "Submarine";
        else shipType = "Destroyer";

        return shipType;
    }

    /**
     * method to throw alert when error occurs
     * @param exceptionID id of exception
     * @param errMessage message to be thrown
     */
    public void throwAlert(String exceptionID, String errMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(exceptionID + "Error!");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(errMessage);
        alert.showAndWait();
        isOK = false;
        initialize();
    }

    /**
     * method to check if game is over
     */
    public void checkEndGame() {
        if (hintUsed != 0 && PlayGame.player.availableShoots == 0) {
            setResultLabel(PlayGame.gameEnded());
            iCoordinate.setVisible(false);
            jCoordinate.setVisible(false);
            shootButton.setVisible(false);
            enterCoords.setVisible(false);
            updateGameInfo(true);
            showEnemyShipsLeft();
            gameRunning = false;
        }
        if ((PlayGame.player.availableShoots == 0 && PlayGame.enemy.availableShoots == 0) || PlayGame.player.intactShips == 0 || PlayGame.enemy.intactShips == 0) {
            setResultLabel(PlayGame.gameEnded());
            iCoordinate.setVisible(false);
            jCoordinate.setVisible(false);
            shootButton.setVisible(false);
            enterCoords.setVisible(false);
            updateGameInfo(true);
            showEnemyShipsLeft();
            gameRunning = false;
        }
    }

    /**
     * method to show the enemy ships left when game ends
     */
    private void showEnemyShipsLeft() {
        ArrayList<Pair<Integer, Integer>> battleshipPos = PlayGame.enemy.battleship.positions;
        ArrayList<Pair<Integer, Integer>> cruiserPos = PlayGame.enemy.cruiser.positions;
        ArrayList<Pair<Integer, Integer>> destroyerPos = PlayGame.enemy.destroyer.positions;
        ArrayList<Pair<Integer, Integer>> carrierPos = PlayGame.enemy.carrier.positions;
        ArrayList<Pair<Integer, Integer>> submarinePos = PlayGame.enemy.submarine.positions;

        showEnemyShipsLeftAux(battleshipPos);
        showEnemyShipsLeftAux(cruiserPos);
        showEnemyShipsLeftAux(destroyerPos);
        showEnemyShipsLeftAux(carrierPos);
        showEnemyShipsLeftAux(submarinePos);

    }

    /**
     * auxiliary method for showEnemyShipsLeft
     * @param shipPos arraylist with all positions of that ship
     */
    private void showEnemyShipsLeftAux(ArrayList<Pair<Integer, Integer>> shipPos) {
        if (shipPos.size() != 0)
            for (Pair<Integer, Integer> Pos : shipPos)
                enemyCells[Pos.getX() + 1][Pos.getY() + 1].setFill(Color.GOLD);
    }

    /**
     * method to handle on click event on enemy grid so as player can make a move by clicking
     * on enemy grid
     * @param e is the MouseEvent
     */
    public void onClickEvent(MouseEvent e) {
        setResultLabel("");
        Cell cell = (Cell) e.getSource();
        cell.i--;
        cell.j--;
        if (!playgame.playerTurn(cell.i, cell.j)) {
            setResultLabel("This position was already hit");
            return;
        }
        playerShotsList.add(new Pair<>(cell.i, cell.j));
        enemyCells[cell.i + 1][cell.j + 1].updateCell(PlayGame.enemy.grid.grid[cell.i][cell.j]);

        makeMoveEnemy();
    }

    /**
     * method used to make an enemy (bot) move
     */
    private void makeMoveEnemy() {
        Triplet<Integer, Integer, Integer> enemyHit;

        checkEndGame();
        if (gameRunning) {
            enemyHit = playgame.enemyTurn(difficulty);
            enemyShotsList.add(new Pair<>(enemyHit.getX(), enemyHit.getY()));
            playerCells[enemyHit.getX() + 1][enemyHit.getY() + 1].updateCell(PlayGame.player.grid.grid[enemyHit.getX()][enemyHit.getY()]);
            checkEndGame();
        }
        updateGameInfo(true);
    }

    /**
     * method to update game information
     * @param all if is set to true, will update all game info, if false will update only enemy's info
     */
    public void updateGameInfo(boolean all) {
        if (all) setAliveShipsPlayer(PlayGame.player.intactShips);
        setAliveShipsEnemy(PlayGame.enemy.intactShips);
        if (all) setPointsPlayer(PlayGame.player.points);
        setPointsEnemy(PlayGame.enemy.points);
        if (all) setAvailableShootsPlayer(PlayGame.player.availableShoots);
        setAvailableShootsEnemy(PlayGame.enemy.availableShoots);
        if (all) setSuccessfulShotsRatePlayer(String.format("%.2f", ((double) PlayGame.player.successfulShoots/(40 - PlayGame.player.availableShoots))*100));
        setSuccessfulShotsRateEnemy(String.format("%.2f", ((double) PlayGame.enemy.successfulShoots/(40 - PlayGame.enemy.availableShoots))*100));
    }

    /**
     * method to initialize game, called by initialize and when player want to restart the game
     */
    public void initialize() {
        playerCells = new Cell[11][11];
        enemyCells = new Cell[11][11];
        playgame = new PlayGame();
        gameRunning = true;
        hintUsed = 0;
        playerShotsList = new ArrayList<>();
        enemyShotsList = new ArrayList<>();
        Triplet<Integer, Integer, Integer> enemyCoords;

        // init game and labels
        if (Scenario_ID == null || !isOK) {
            Scenario_ID = "default";
            PlayGame.startGame(Scenario_ID, this);
            isOK = true;
        }
        else PlayGame.startGame(Scenario_ID, this);
        if (difficulty == 1) setDifficultyLabel("Mode: Easy");
        else if (difficulty == 2) setDifficultyLabel("Mode: Medium");
        else if (difficulty == 3) setDifficultyLabel("Mode: Hard");
        else setDifficultyLabel("Mode: Impossible");
        iCoordinate.setText("");
        jCoordinate.setText("");
        iCoordinate.setVisible(true);
        jCoordinate.setVisible(true);
        shootButton.setVisible(true);
        enterCoords.setVisible(true);
        setEnterCoords("Enter coordinates or click on EnemyGrid to make a move:");
        setPlayerScenarioLabel("Player: player_" + Scenario_ID + ".txt");
        setEnemyScenarioLabel("Enemy: enemy_" + Scenario_ID + ".txt");

        updateGameInfo(true);
        setSuccessfulShotsRatePlayer("0.00");
        setSuccessfulShotsRateEnemy("0.00");

        for (int i = 0; i < 11; i++){
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j == 0) {}
                else if (i == 0) {
                    createLabel(j, i, j, gridPlayer);
                    createLabel(j, i, j, gridEnemy);
                }
                else if (j == 0) {
                    createLabel(i, i, j, gridPlayer);
                    createLabel(i, i, j, gridEnemy);
                }
                else {
                    playerCells[i][j] = new Cell();
                    enemyCells[i][j] = new Cell(i, j);
                    enemyCells[i][j].setOnMouseClicked(e -> {
                        if (!gameRunning) return;
                        onClickEvent(e);
                    });
                    if (PlayGame.player.grid.shipTypeAtPos(i-1,j-1, PlayGame.player) >= 1 &&
                            PlayGame.player.grid.shipTypeAtPos(i-1,j-1, PlayGame.player) <= 5){
                        playerCells[i][j].isShip();
                    }
                    gridPlayer.add(playerCells[i][j], j, i);
                    gridEnemy.add(enemyCells[i][j], j, i);
                }
            }
        }
        move = PlayGame.coinFlip();
        if (move) setResultLabel("You are doing the first move!");
        else {
            setResultLabel("Computer is doing the first move!");
            enemyCoords = playgame.enemyTurn(difficulty);
            enemyShotsList.add(new Pair<>(enemyCoords.getX(), enemyCoords.getY()));
            playerCells[enemyCoords.getX() + 1][enemyCoords.getY() + 1].updateCell(PlayGame.player.grid.grid[enemyCoords.getX()][enemyCoords.getY()]);
            updateGameInfo(false);
        }
    }

    /**
     * method to create labels for numbering in enemyGrid, playerGrid
     * @param x is either i or j, depending on where it was called
     * @param i i coord
     * @param j j coord
     * @param gridPane object
     */
    private void createLabel(int x, int i, int j, GridPane gridPane) {
        Label label = new Label();
        label.setMinHeight(40.0);
        label.setMinWidth(40.0);
        label.setAlignment(Pos.CENTER);
        label.setText(Integer.toString(x - 1));
        label.setFont(new Font("Arial", 20));
        gridPane.add(label, j, i);
    }

    public static void main(String[] args) { launch(args); }

    /**
     * @param primaryStage primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try{
            URL url = new File("src/battleship/battleshipGui.fxml").toURI().toURL();
            root = FXMLLoader.load(url);
        } catch(Exception e){e.printStackTrace();}
        primaryStage.setScene(new Scene(root,1200,900));
        primaryStage.setTitle("MediaLab Battleship");
        primaryStage.setResizable(false);
        String background_color = "whitesmoke";
        root.setStyle("-fx-background-color:" + background_color + ";");
        primaryStage.show();
    }

}