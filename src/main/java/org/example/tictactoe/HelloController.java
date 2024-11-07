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

public class HelloController {
    public GridPane gridPane;
    private static final Model model = new Model();
    private GameNetworkService gameNetworkService;
    public static boolean isGameOver = false;
    private static int scoreO;
    private static int scoreX;
    private char lastPlayer;

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
    private void resetButton() {
        resetBoard();
    }

    @FXML
    public void initialize() {
        displayMessage("Welcome to Tic Tac Toe");
        showScore();
        lastPlayer = 'O';
    }

    public void startServer(int port) {
        Servero server = new Servero();
        gameNetworkService = new GameNetworkService(server);

        new Thread(() -> {
            try {
                server.startServer(port);
                while (!isGameOver) {
                    String message = gameNetworkService.receiveMessage();
                    handleReceivedMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void startClient(String ip, int port) {
        Cliento client = new Cliento();
        gameNetworkService = new GameNetworkService(client);

        new Thread(() -> {
            try {
                client.startClient(ip, port);
                while (!isGameOver) {
                    String message = gameNetworkService.receiveMessage();
                    handleReceivedMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleReceivedMessage(String message) {
        if ("WIN_SIGNAL".equals(message)) {
            Platform.runLater(() -> {
                updateScore();
                showScore();
                showWinText();
            });
            isGameOver = true;
        } else {
            int[] move = gameNetworkService.isServer() ? gameNetworkService.getServer().parseMove(message)
                    : gameNetworkService.getClient().parseMove(message);

            Platform.runLater(() -> {
                try {
                    makeOpponentMove(move[0], move[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            model.switchPlayer();
        }
    }

    public void playerMove(ActionEvent event) throws IOException {
        if (isGameOver) return;

        if ((gameNetworkService.isServer() && model.getCurrentPlayer() != Model.PLAYER_SERVER) ||
                (!gameNetworkService.isServer() && model.getCurrentPlayer() != Model.PLAYER_CLIENT)) {
            displayMessage("It's not your turn!");
            return;
        }

        Button clickedButton = (Button) event.getSource();
        Integer row = GridPane.getRowIndex(clickedButton);
        Integer col = GridPane.getColumnIndex(clickedButton);

        if (model.makeMove(row, col)) {
            lastPlayer = model.getCurrentPlayer();
            clickedButton.setText(String.valueOf(model.getCurrentPlayer() == Model.PLAYER_SERVER ? 'X' : 'O'));
            if (gameCompleted()) return;
            gameNetworkService.sendMove(row, col);
            if (gameCompleted()) {
                getScore();
            }
        }
    }

    void makeOpponentMove(int row, int col) throws IOException {
        if (model.receiveOpponentMove(row, col)) {
            for (Node node : gridPane.getChildren()) {
                if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                    Button button = (Button) node;
                    button.setText(String.valueOf(model.getCurrentPlayer()));
                    break;
                }
            }
            if (gameCompleted()) {
                getScore();
            }
            model.switchPlayer();
        }
    }

    public void getScore() {
        String score = showScore();
        displayScoreForBoth(score);
        gameNetworkService.sendWinSignal();
    }

    public void displayScoreForBoth(String score) {
        Platform.runLater(() -> {
            scoreTextFlow.getChildren().clear();
            Text scoreText = new Text(score);
            scoreText.setStyle("-fx-fill: #7FFF00;");
            scoreTextFlow.getChildren().add(scoreText);
        });
    }

    private boolean gameCompleted() {
        if (model.isGameWon(lastPlayer)) {
            isGameOver = true;
            updateScore();
            showWinText();
            showScore();
            gameNetworkService.sendWinSignal();
            return true;
        } else if (model.isBoardFull()) {
            isGameOver = true;
            showDrawText();
            return true;
        }
        return false;
    }

    public void displayMessage(String message) {
        textFlow.getChildren().clear();
        Text text = new Text(message);
        textFlow.getChildren().add(text);
        text.setStyle("-fx-fill: #7FFF00;");
    }

    void showWinText() {
        displayMessage("Player " + lastPlayer + " wins!");
    }

    private void showDrawText() {
        displayMessage("It's a draw!");
    }

    String showScore() {
        scoreTextFlow.getChildren().clear();
        String scoreString = "Points: Player O: " + scoreO + ", Player X: " + scoreX;
        Text scoreText = new Text(scoreString);
        scoreTextFlow.getChildren().add(scoreText);
        scoreText.setStyle("-fx-fill: #7FFF00;");
        return scoreString;
    }

    void updateScore() {
        if (lastPlayer == Model.PLAYER_SERVER) {
            scoreO++;
        } else {
            scoreX++;
        }
    }

    private void resetBoard() {
        isGameOver = false;
        model.initializeBoard();
        scoreO = 0;
        scoreX = 0;

        gridPane.getChildren().stream()
                .filter(node -> node instanceof Button)
                .forEach(node -> ((Button) node).setText(""));

        initialize();

        // Återställ server och klient om det behövs
        if (gameNetworkService.isServer()) {
            restartServer();
        } else if (gameNetworkService.isClient()) {
            restartClient();
        }
    }

    private void restartClient() {
        try {
            gameNetworkService.stop();  // Stänger den gamla klienten
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Starta om klienten
        Cliento client = new Cliento();
        gameNetworkService = new GameNetworkService(client);
        new Thread(() -> {
            try {
                client.startClient("localhost", 8080);
                while (!isGameOver) {
                    String message = gameNetworkService.receiveMessage();
                    handleReceivedMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void restartServer() {
        try {
            gameNetworkService.stop();  // Stänger den gamla servern
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Starta om servern
        Servero server = new Servero();
        gameNetworkService = new GameNetworkService(server);
        new Thread(() -> {
            try {
                server.startServer(8080);
                while (!isGameOver) {
                    String message = gameNetworkService.receiveMessage();
                    handleReceivedMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }



}
