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
    public static boolean isGameOver = false;
    private static int scoreO;
    private static int scoreX;
    private static Servero server;
    private static Cliento client;
    private static boolean isServer;
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
        displayMessage("Server starts");
        showScore();
    }

    public void startServer(int port) {
        server = new Servero();
        isServer = true;
        new Thread(() -> {
            try {
                server.startServer(port);
                while (!isGameOver) {
                    String message = server.receiveMessage();
                    if ("WIN_SIGNAL".equals(message)) {
                        Platform.runLater(() -> {
                            updateScore();   // Uppdatera poängen lokalt
                            showScore();
                            showWinText();// Visa den uppdaterade poängen
                        });
                        break;
                    }
                    int[] move = server.parseMove(message); // Hantera draget om det inte är en vinstsignal
                    Platform.runLater(() -> {
                        try {
                            makeOpponentMove(move[0], move[1]);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
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
                    String message = client.receiveMessage();  // Använd client här istället för server
                    if ("WIN_SIGNAL".equals(message)) {
                        Platform.runLater(() -> {
                            updateScore();   // Uppdatera poängen lokalt
                            showScore();
                            showWinText();// Visa den uppdaterade poängen
                        });
                        break;
                    }
                    int[] move = client.parseMove(message); // Använd client här för att tolka draget
                    Platform.runLater(() -> {
                        try {
                            makeOpponentMove(move[0], move[1]);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    model.switchPlayer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void playerMove(ActionEvent event) throws IOException {
        if (isGameOver) return;

        // Kontrollera att det är den aktuella spelarens tur
        if ((isServer && model.getCurrentPlayer() != Model.PLAYER_SERVER) ||
                (!isServer && model.getCurrentPlayer() != Model.PLAYER_CLIENT)) {
            displayMessage("It's not your turn!");
            return;
        }

        Button clickedButton = (Button) event.getSource();
        Integer row = GridPane.getRowIndex(clickedButton);
        Integer col = GridPane.getColumnIndex(clickedButton);

        if (model.makeMove(row, col)) {
            // Sätt texten på knappen baserat på vilken spelare som gjorde draget
            lastPlayer = model.getCurrentPlayer();
            clickedButton.setText(String.valueOf(model.getCurrentPlayer() == Model.PLAYER_SERVER ? 'X' : 'O'));
            if (gameCompleted()) return;
            lastPlayer = model.getCurrentPlayer();
            sendMoveToOpponent(row, col);
            if (gameCompleted()){
                getScore();
                return;
            }
        }
    }

    private void makeOpponentMove(int row, int col) throws IOException {
        if (model.receiveOpponentMove(row, col)) {                                                  //kollar om draget är giltigt
            for (Node node : gridPane.getChildren()) {
                if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {    //går igenom knapparna i gridpane
                    Button button = (Button) node;
                    button.setText(String.valueOf(model.getCurrentPlayer()));                       //sätter currentplayer på platsen
                    break;
                }
            }
            if (gameCompleted()){
                getScore();
                return;
            }
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

    public void getScore() {
        String score = showScore(); // Uppdatera och visa poängen lokalt
        displayScoreForBoth(score); // Uppdatera poängvisningen för båda spelarna

        // Skicka signal till motståndaren om att spelet är slut
        if (isServer) {
            server.sendWinSignal();
        } else {
            client.sendWinSignal();
        }
    }

    public void displayScoreForBoth(String score) {
        Platform.runLater(() -> {
            scoreTextFlow.getChildren().clear(); // Rensa poängtextfältet innan uppdatering
            Text scoreText = new Text(score);
            scoreText.setStyle("-fx-fill: #7FFF00;");
            scoreTextFlow.getChildren().add(scoreText); // Lägg till den nya poängtexten
        });
    }


    private boolean gameCompleted() {
        if (model.isGameWon(lastPlayer)) {
            isGameOver = true;
            updateScore();  // Uppdatera poängen lokalt
            showWinText();
            showScore();

            // Skicka vinstsignal till motståndaren för att denne också kan uppdatera poängen
            sendWinSignalToOpponent();

            return true;
        } else if (model.isBoardFull()) {
            isGameOver = true;
            showDrawText();
            return true;
        }
        return false;
    }

    public void sendWinSignalToOpponent() {
        if (isServer) {
            server.sendWinSignal();
        } else {
            client.sendWinSignal();
        }
    }


    public void displayMessage(String message) {
        textFlow.getChildren().clear();
        Text text = new Text(message);
        textFlow.getChildren().add(text);
        text.setStyle("-fx-fill: #7FFF00;");
    }

    private void showWinText() {
        displayMessage("Player " + lastPlayer + " wins!");
    }

    private void showDrawText() {
        displayMessage("It's a draw!");
    }

    private String showScore() {
        scoreTextFlow.getChildren().clear();
        String scoreString = "Points: Player O: " + scoreO + ", Player X: " + scoreX;
        Text scoreText = new Text(scoreString);
        scoreTextFlow.getChildren().add(scoreText);
        scoreText.setStyle("-fx-fill: #7FFF00;");
        return scoreString;
    }

    private void updateScore() {
        if (lastPlayer == Model.PLAYER_SERVER) {
            scoreO++;
        } else {
            scoreX++;
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
