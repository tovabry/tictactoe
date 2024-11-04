package org.example.tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import org.example.tictactoe.socketo.Cliento;
import org.example.tictactoe.socketo.Servero;

import java.io.IOException;
import java.util.Objects;

public class HelloController {
    public GridPane gridPane;
    private static final Model model = new Model();
    public static boolean isGameOver = false;
    private static int scoreO;
    private static int scoreX;
    private static Servero server;
    private static Cliento client;
    private static boolean isServer;

    @FXML
    private TextFlow textFlow;
    @FXML
    private TextFlow scoreTextFlow;
    @FXML
    public void onHostButtonClick() {
        startServer(8080); // Start server on button click
    }
    @FXML
    public void onJoinButtonClick() {
        startClient("localhost", 8080); // Start client on button click
    }

    @FXML
    public void initialize() {
        displayMessage("Make your move...");
        showScore();
    }

    public void startServer(int port) {
        server = new Servero();
        isServer = true;
        new Thread(() -> {
            try {
                server.startServer(port);
                while (!isGameOver) {
                    int[] move = server.receiveMove();
                    Platform.runLater(() -> makeOpponentMove(move[0], move[1]));
                    model.switchPlayer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void startClient(String ip, int port) {
        client = new Cliento();
        isServer = false;
        new Thread(() -> {
            try {
                client.startClient(ip, port);
                while (!isGameOver) {
                    int[] move = client.receiveMove();
                    Platform.runLater(() -> makeOpponentMove(move[0], move[1]));
                    model.switchPlayer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void playerMove(ActionEvent event) {
        if (isGameOver) return;

        // Kontrollera att det är den aktuella spelarens tur
        if ((isServer && !model.getCurrentPlayer().equals(Model.PLAYER_SERVER)) ||
                (!isServer && !model.getCurrentPlayer().equals(Model.PLAYER_CLIENT))) {
            displayMessage("It's not your turn!");
            return;
        }

        Button clickedButton = (Button) event.getSource();
        Integer row = GridPane.getRowIndex(clickedButton);
        Integer col = GridPane.getColumnIndex(clickedButton);

        if (model.makeMove(row, col)) {
            clickedButton.setText(String.valueOf(model.getCurrentPlayer().equals(Model.PLAYER_SERVER) ? 'X' : 'O'));
            sendMoveToOpponent(row, col);
        }
    }


    private void makeOpponentMove(int row, int col) {
        if (model.receiveOpponentMove(row, col)) {
            for (Node node : gridPane.getChildren()) {
                if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                    Button button = (Button) node;
                    button.setText(String.valueOf(model.getCurrentPlayer()));
                    break;
                }
            }
            if (gameCompleted()) return;
            model.switchPlayer();
        }
    }

    public static void sendMoveToOpponent(int row, int col) {
        if (isServer) {
            server.sendMove(row, col); // Om denna instans är server, skicka draget via servern
        } else {
            client.sendMove(row, col); // Annars skicka draget via klienten
        }
    }



//    private void computerMove() {
//        if (isGameOver) return;
//
//        int[] computerMove = model.getComputerMove();
//        if (computerMove != null) {
//            int computerRow = computerMove[0];
//            int computerCol = computerMove[1];
//
//            model.makeMove(computerRow, computerCol);
//            for (Node node : gridPane.getChildren()) {
//                if (GridPane.getRowIndex(node) == computerRow && GridPane.getColumnIndex(node) == computerCol) {
//                    Button button = (Button) node;
//                    button.setText(String.valueOf(Model.PLAYER_O)); // Datorns karaktär
//                    break;
//                }
//            }
//
//            if (gameCompleted()) return;
//            model.switchPlayer();
//        }
//    }

    private boolean gameCompleted() {
        if (model.isGameWon(model.getCurrentPlayer())) {
            isGameOver = true;
            showWinText();
            updateScore();
            showScore();
            return true;
        } else if (model.isBoardFull()) {
            isGameOver = true;
            showDrawText();
            return true;
        }
        return false;
    }

    @FXML
    private void resetButton() {
        resetBoard();
    }

    public void displayMessage(String message) {
        textFlow.getChildren().clear();
        Text text = new Text(message);
        textFlow.getChildren().add(text);
        text.setStyle("-fx-fill: #7FFF00;");
    }

    private void showWinText() {
        displayMessage("Player " + model.getCurrentPlayer() + " wins!");
    }

    private void showDrawText() {
        displayMessage("It's a draw!");
    }

    private void showScore() {
        scoreTextFlow.getChildren().clear();
        String scoreString = "Points: Player O: " + scoreO + ", Player X: " + scoreX;
        Text scoreText = new Text(scoreString);
        scoreTextFlow.getChildren().add(scoreText);
        scoreText.setStyle("-fx-fill: #7FFF00;");
    }

    private static void updateScore() {
        if (Objects.equals(model.getCurrentPlayer(), Model.PLAYER_SERVER)) {
            scoreX++;
        } else {
            scoreO++;
        }
    }

    private void resetBoard() {
        model.initializeBoard();
        gridPane.getChildren().stream()
                .filter(node -> node instanceof Button)
                .forEach(node -> ((Button) node).setText(""));
        textFlow.getChildren().clear();
        isGameOver = false;
        initialize();
    }

}
