package org.example.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;

public class HelloController {
    public GridPane gridPane;
    private final Model model = new Model();
    private boolean isGameOver = false;
    private int scoreO;
    private int scoreX;

    @FXML
    private TextFlow textFlow;
    @FXML
    private TextFlow scoreTextFlow;

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
                    button.setText(String.valueOf(model.getCurrentPlayer()));
                    break;
                }
            }
            gameCompleted();
            model.switchPlayer();
        }
    }

    private boolean gameCompleted() {
        if (model.isGameWon()) {
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
    private void resetButton(){
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

    private void showScore(){
        scoreTextFlow.getChildren().clear();
        String string = ("Points: Player O: " + scoreO + ", Player x: " + scoreX);
        Text scoreText = new Text(string);
        scoreTextFlow.getChildren().add(scoreText);
        scoreText.setStyle("-fx-fill: #7FFF00;");
    }

    private void updateScore(){
        if (model.isGameWon())
          if  (model.getCurrentPlayer() == 'X')
              scoreX++;
          else
              scoreO++;
    }

    private void resetBoard() {
        model.initializeBoard();
        gridPane.getChildren().stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .forEach(button -> button.setText(""));
        textFlow.getChildren().clear();
        isGameOver = false;
        initialize();
    }
}