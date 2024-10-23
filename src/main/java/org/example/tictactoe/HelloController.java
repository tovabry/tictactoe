package org.example.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HelloController {
    public GridPane gridPane;
    private Model model = new Model();

    @FXML
    private Button button00, button01, button02, button10, button11, button12, button20, button21, button22;

    @FXML
    public void handleButtonClick(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        Integer row = GridPane.getRowIndex(clickedButton);
        Integer col = GridPane.getColumnIndex(clickedButton);

        if (row == null || col == null) {
            return; // Om row eller col är null, gör inget
        }

        // Spelarens drag
        if (model.makeMove(row, col)) {
            clickedButton.setText(String.valueOf(model.getCurrentPlayer()));

            if (model.isGameWon()) {
                showEndGameDialog("Player " + model.getCurrentPlayer() + " wins!");
                resetBoard();
                return;
            } else if (model.isBoardFull()) {
                showEndGameDialog("It's a draw!");
                resetBoard();
                return;
            }

            // Växla till datorns drag
            model.switchPlayer();

            // Datorns drag
            int[] computerMove = model.getComputerMove();
            if (computerMove != null) {
                int computerRow = computerMove[0];
                int computerCol = computerMove[1];

                // Uppdatera spelbrädet med datorns drag
                model.makeMove(computerRow, computerCol);

                // Hitta rätt knapp och uppdatera dess text
                for (Node node : gridPane.getChildren()) {
                    if (GridPane.getRowIndex(node) == computerRow && GridPane.getColumnIndex(node) == computerCol) {
                        Button button = (Button) node;
                        button.setText(String.valueOf(model.getCurrentPlayer()));
                        break;
                    }
                }

                showWinText();
                showDrawText();

                // Växla tillbaka till spelarens tur
                model.switchPlayer();
            }
        }
    }

    private void showWinText() {
        if (model.isGameWon()) {
            showEndGameDialog("Player " + model.getCurrentPlayer() + " wins!");
        }
    }

    private void showDrawText() {
        if (model.isBoardFull()) {
            showEndGameDialog("It's a draw!");
        }
    }

    private void showEndGameDialog(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    }
}


//
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.text.Text;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//import java.util.ResourceBundle;
//
//public class HelloController implements Initializable {
//
//    Random random = new Random();
//    ArrayList<Button> buttons;
//
//    //@FXML
//    private Button button1;
//    private Button button2;
//    private Button button3;
//    private Button button4;
//    private Button button5;
//    private Button button6;
//    private Button button7;
//    private Button button8;
//    private Button button9;
//    private Text winnerText;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
//
//
////        buttons.forEach(button ->{
////            setupButton(button);
////            button.setFocusTraversable(false);
////        });
//
//    }
//
////    private void setupButton(Button button) {
////        button.setOnMouseClicked(mouseEvent -> {
////            button.setText("O");
////            button.setDisable(true);
////        });
////    }
//
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
//}