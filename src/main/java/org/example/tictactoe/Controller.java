package org.example.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;

public class Controller {
    public GridPane gridPane;
    private final Model model = new Model();
    public boolean isGameOver = false;
    private int scoreO;
    private int scoreX;

    @FXML
    TextFlow textFlow;
    @FXML
    private TextFlow scoreTextFlow;
    @FXML
    private void resetButton() {
        resetBoard();
    }

    @FXML
    public void initialize() {
        displayMessage("Make your move...");
        showScore();
    }

    @FXML
    public void playerMove(javafx.event.ActionEvent event) {
        if (isGameOver) return;
        Button clickedButton = (Button) event.getSource();

        Integer row = GridPane.getRowIndex(clickedButton);
        Integer col = GridPane.getColumnIndex(clickedButton);

        if (model.makeMove(row, col)) {
            clickedButton.setText(String.valueOf(model.getCurrentPlayer()));

            if (gameCompleted()) return;
            model.switchPlayer();
            computerMove();
        }
    }

    private void computerMove() {
        if (isGameOver) return;

        int[] computerMove = model.getComputerMove();
        if (computerMove != null) {
            int computerRow = computerMove[0];
            int computerCol = computerMove[1];

            model.makeMove(computerRow, computerCol);
            for (Node node : gridPane.getChildren()) {
                if (GridPane.getRowIndex(node) == computerRow && GridPane.getColumnIndex(node) == computerCol) {
                    Button button = (Button) node;
                    button.setText(String.valueOf(Model.PLAYER_O)); // Datorns karaktär
                    break;
                }
            }

            if (gameCompleted()) return;
            model.switchPlayer();
        }
    }

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

    private void updateScore() {
        if (model.getCurrentPlayer() == Model.PLAYER_X) {
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
