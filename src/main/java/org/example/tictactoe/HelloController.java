package org.example.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;

public class HelloController {
    public GridPane gridPane;
    private Model model = new Model();
    private boolean isGameOver = false;
    @FXML
    private Button button00, button01, button02, button10, button11, button12, button20, button21, button22;

    @FXML
    private TextFlow textFlow;

    @FXML
    public void initialize() {
        displayMessage("Make your move...");
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

    private void showWinText() {
        if (model.isGameWon()) {
            textFlow.getChildren().clear();
            displayMessage("Player " + model.getCurrentPlayer() + " wins!");
        }
    }

    private void showDrawText() {
        if (model.isBoardFull()) {
            textFlow.getChildren().clear();
            displayMessage("It's a draw!");
        }
    }

    public void displayMessage(String message) {
        Text text = new Text(message);
        textFlow.getChildren().add(text);
        text.setStyle("-fx-fill: #7FFF00;");
        }


    private void resetBoard() {
        model.initializeBoard();
        button00.setText("");
        button01.setText("");
        button02.setText("");
        button10.setText("");
        button11.setText("");
        button12.setText("");
        button20.setText("");
        button21.setText("");
        button22.setText("");
        textFlow.getChildren().clear();
        initialize();
    }
}