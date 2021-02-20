package battleship;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.File;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.util.Duration;

public class Main extends Application implements Initializable {

    public boolean move;
    public boolean isOK;
    private static int hintUsed = 0;
    private static PlayGame playgame;
    public Cell[][] playerCells;
    public Cell[][] enemyCells;
    public static int difficulty = 2;
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
    private Label difficultyLabel;
    @FXML
    private Label playerScenario;
    @FXML
    private Label enemyScenario;
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
    public void setDifficultyLabel(String s) { this.difficultyLabel.setText(s); }
    public void setPlayerScenarioLabel(String s) { this.playerScenario.setText(s); }
    public void setEnemyScenarioLabel(String s) { this.enemyScenario.setText(s); }
    private void setAliveShipsEnemy(int intactShips) { this.aliveShipsPlayer.setText("Ships Alive: " + intactShips); }
    private void setAliveShipsPlayer(int intactShips) { this.aliveShipsEnemy.setText("Ships Alive: " + intactShips); }
    private void setPointsPlayer(int points) { this.pointsPlayer.setText("Points: " + points); }
    private void setPointsEnemy(int points) { this.pointsEnemy.setText("Points: " + points); }
    private void setAvailableShootsPlayer(int shoots) { this.availableShootsPlayer.setText("Available Shoots: " + shoots); }
    private void setAvailableShootsEnemy(int shoots) { this.availableShootsEnemy.setText("Available Shoots: " + shoots); }
    private void setSuccessfulShotsRatePlayer(String rate) { this.successfulShotsRatePlayer.setText("Successful Shots Rate: " + rate); }
    private void setSuccessfulShotsRateEnemy(String rate) { this.successfulShotsRateEnemy.setText("Successful Shots Rate: " + rate); }

    public class Cell extends Rectangle {
        public Cell() {
            super(40, 40);
            setStroke(Color.BLACK);
            setFill(Color.WHITE);
        }

        public void isShip() { setFill(Color.YELLOWGREEN); }
        public void shipHit() { setFill(Color.RED); }
        public void seaHit() { setFill(Color.BLUE); }

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

    public void startAction(ActionEvent t){
        initialize();
    }

    public void loadAction(ActionEvent t) {
        TextInputDialog lDialog = new TextInputDialog("Scenario_ID");
        lDialog.setHeaderText("Enter your game Scenario_ID (number):");
        lDialog.showAndWait();
        Scenario_ID = lDialog.getEditor().getText();
        initialize();
    }

    public void easy(ActionEvent t){
        difficulty = 1;
        initialize();
        setDifficultyLabel("Mode: Easy");
    }

    public void medium(ActionEvent t){
        difficulty = 2;
        initialize();
        setDifficultyLabel("Mode: Medium");
    }

    public void hard(ActionEvent t){
        difficulty = 3;
        initialize();
        setDifficultyLabel("Mode: Hard");
    }

    public void impossible(ActionEvent t){
        difficulty = 4;
        initialize();
        setDifficultyLabel("Mode: Impossible");
    }

    public void exitAction(ActionEvent t){ Platform.exit(); }

    public void enemyShipsAction(ActionEvent t) {
        String enemyShipsStatus = "";
        enemyShipsStatus += "Carrier: " + playgame.enemy.carrier.checkStatus() + "\n";
        enemyShipsStatus += "Battleship: " + playgame.enemy.battleship.checkStatus() + "\n";
        enemyShipsStatus += "Cruiser: " + playgame.enemy.cruiser.checkStatus() + "\n";
        enemyShipsStatus += "Submarine: " + playgame.enemy.submarine.checkStatus() + "\n";
        enemyShipsStatus += "Destroyer: " + playgame.enemy.destroyer.checkStatus() + "\n";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enemy Ships Condition");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(enemyShipsStatus);
        alert.showAndWait();

    }

    public void playerShotsAction(ActionEvent t){
        int lowBound, iCoord, jCoord, shotRes, shipTypeVal = 0;
        String playerShotsText = "", shipType;
        System.out.println(playerShotsList.size());
        if (playerShotsList.size() < 5) lowBound = 0;
        else lowBound = playerShotsList.size() - 5;
        for (int i = playerShotsList.size() - 1; i >= lowBound; i --) {
            iCoord = playerShotsList.get(i).getX();
            jCoord = playerShotsList.get(i).getY();
            shotRes = playgame.enemy.grid.shipTypeAtPos(iCoord, jCoord, playgame.enemy);

            if (shotRes == 6) {
                for (Pair<Integer, Pair<Integer, Integer>> shipPos : playgame.allPlayerShips) {
                    if (shipPos.getY().getX() == iCoord && shipPos.getY().getY() == jCoord) {
                        shipTypeVal = shipPos.getX();
                        break;
                    }
                }
            }

            shipType = findShipType(shipTypeVal);

            playerShotsText += "Position: (" + String.valueOf(iCoord) + "," + String.valueOf(jCoord) + ") "
                    + ((shotRes == 6) ? "Hit a Ship of type " + shipType : "Missed" + '\n');
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Player Shots Info");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        if (playerShotsText == "") alert.setContentText("There are no previous moves made by you.");
        else alert.setContentText(playerShotsText);
        alert.showAndWait();
    }

    public void enemyShotsAction(ActionEvent t){
        int lowBound, iCoord, jCoord, shotRes, shipTypeVal = 0;
        String enemyShotsText = "", shipType;
        System.out.println(enemyShotsList.size());
        if (enemyShotsList.size() < 5) lowBound = 0;
        else lowBound = enemyShotsList.size() - 5;
        for (int i = enemyShotsList.size() - 1; i >= lowBound; i --) {
            iCoord = enemyShotsList.get(i).getX();
            jCoord = enemyShotsList.get(i).getY();
            shotRes = playgame.player.grid.shipTypeAtPos(iCoord, jCoord, playgame.player);

            if (shotRes == 6) {
                for (Pair<Integer, Pair<Integer, Integer>> shipPos : playgame.allEnemyShips) {
                    if (shipPos.getY().getX() == iCoord && shipPos.getY().getY() == jCoord) {
                        shipTypeVal = shipPos.getX();
                        break;
                    }
                }
            }

            shipType = findShipType(shipTypeVal);

            enemyShotsText += "Position: (" + String.valueOf(iCoord) + "," + String.valueOf(jCoord) + ") "
                    + ((shotRes == 6) ? "Hit a Ship of type " + shipType : "Missed" + '\n');
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enemy Shots Info");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        if (enemyShotsText == "") alert.setContentText("There are no previous moves made by Enemy.");
        else alert.setContentText(enemyShotsText);
        alert.showAndWait();
    }

    public void instrAction(ActionEvent t){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("General Info");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("You enter coordinates and press shoot. \n" +
                "You win/lose when all of your ships or enemy ships are down, or when you complete 40 shots. \n" +
                "You can set the difficulty of the bot from Application/Difficulty. \n" +
                "When you hit an enemy ship, the tile is marked red. \n" +
                "When you hit sea, the tile is marked blue.");
        alert.showAndWait();
    }

    public void getHintAction(ActionEvent t){
        hintUsed++;
        Pair<Integer, Integer> coords = playgame.enemy.help(playgame.enemy);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.005), evt -> enemyCells[coords.getX() + 1][coords.getY() + 1].setFill(Color.GREEN)),
                new KeyFrame(Duration.seconds(1), evt -> enemyCells[coords.getX() + 1][coords.getY() + 1].setFill(Color.WHITE)));
        timeline.play();
        playgame.player.availableShoots -= hintUsed;
        setAvailableShootsPlayer(playgame.player.availableShoots);
    }

    public void aboutHintAction(ActionEvent t){
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

    public void shootAction (ActionEvent t) {
        setResultLabel("");

        checkEndGame();

        int i = 0, j = 0;
        Triplet<Integer, Integer, Integer> enemyCoords;

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
        enemyCells[i + 1][j + 1].updateCell(playgame.enemy.grid.grid[i][j]);
        checkEndGame();
        enemyCoords = playgame.enemyTurn(difficulty);
        enemyShotsList.add(new Pair<>(enemyCoords.getX(), enemyCoords.getY()));
        playerCells[enemyCoords.getX() + 1][enemyCoords.getY() + 1].updateCell(playgame.player.grid.grid[enemyCoords.getX()][enemyCoords.getY()]);
        checkEndGame();

        setAliveShipsPlayer(playgame.player.intactShips);
        setAliveShipsEnemy(playgame.enemy.intactShips);
        setPointsPlayer(playgame.player.points);
        setPointsEnemy(playgame.enemy.points);
        setAvailableShootsPlayer(playgame.player.availableShoots);
        setAvailableShootsEnemy(playgame.enemy.availableShoots);
        setSuccessfulShotsRatePlayer(String.format("%.2f", ((double)playgame.player.successfulShoots/(40 - playgame.player.availableShoots))*100));
        setSuccessfulShotsRateEnemy(String.format("%.2f", ((double)playgame.enemy.successfulShoots/(40 - playgame.enemy.availableShoots))*100));

    }

    private String findShipType(int shipTypeVal) {
        String shipType = "";

        if (shipTypeVal == 1) shipType = "Carrier";
        else if (shipTypeVal == 2) shipType = "Battleship";
        else if (shipTypeVal == 3) shipType = "Cruiser";
        else if (shipTypeVal == 4) shipType = "Submarine";
        else shipType = "Destroyer";

        return shipType;
    }

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

    public void checkEndGame() {
        if (playgame.player.intactShips == 0 || playgame.player.availableShoots == 0 || playgame.enemy.intactShips == 0 || playgame.enemy.availableShoots == 0)
            setResultLabel(playgame.gameEnded());

    }

    public void initialize() {
        playerCells = new Cell[11][11];
        enemyCells = new Cell[11][11];
        playgame = new PlayGame();
        playerShotsList = new ArrayList<>();
        enemyShotsList = new ArrayList<>();
        Triplet<Integer, Integer, Integer> enemyCoords;

        // init game and labels
        if (Scenario_ID == null || !isOK) {
            Scenario_ID = "default";
            playgame.startGame(Scenario_ID, this);
            isOK = true;
        }
        else playgame.startGame(Scenario_ID, this);
        if (difficulty == 1) setDifficultyLabel("Mode: Easy");
        else if (difficulty == 2) setDifficultyLabel("Mode: Medium");
        else if (difficulty == 3) setDifficultyLabel("Mode: Hard");
        else setDifficultyLabel("Mode: Impossible");
        iCoordinate.setText("");
        jCoordinate.setText("");
        setPlayerScenarioLabel("Player: player_" + Scenario_ID + ".txt");
        setEnemyScenarioLabel("Enemy: enemy_" + Scenario_ID + ".txt");

        setAliveShipsPlayer(playgame.player.intactShips);
        setAliveShipsEnemy(playgame.enemy.intactShips);
        setPointsPlayer(playgame.player.points);
        setPointsEnemy(playgame.enemy.points);
        setAvailableShootsPlayer(playgame.player.availableShoots);
        setAvailableShootsEnemy(playgame.enemy.availableShoots);
        setSuccessfulShotsRatePlayer("0.00");
        setSuccessfulShotsRateEnemy("0.00");

        for (int i = 0; i < 11; i++){
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j == 0) continue;
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
                    if (playgame.player.grid.shipTypeAtPos(i-1,j-1, playgame.player) >= 1 &&
                            playgame.player.grid.shipTypeAtPos(i-1,j-1, playgame.player) <= 5){
                        playerCells[i][j].isShip();
                    }
                    enemyCells[i][j] = new Cell();
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
            playerCells[enemyCoords.getX() + 1][enemyCoords.getY() + 1].updateCell(playgame.player.grid.grid[enemyCoords.getX()][enemyCoords.getY()]);
            setPointsEnemy(playgame.enemy.points);
            setAvailableShootsEnemy(playgame.enemy.availableShoots);
            setSuccessfulShotsRateEnemy(String.format("%.2f", ((double)playgame.enemy.successfulShoots/(40 - playgame.enemy.availableShoots))*100));
        }
    }

    private void createLabel(int x, int i, int j, GridPane gridPane) {
        Label label = new Label();
        label.setMinHeight(40.0);
        label.setMinWidth(40.0);
        label.setAlignment(Pos.CENTER);
        label.setText(Integer.toString(x - 1));
        label.setFont(new Font("Arial", 20));
        gridPane.add(label, j, i);
    }

    public static void main(String[] args) {
        launch(args);
    }

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
        primaryStage.show();

    }

}
