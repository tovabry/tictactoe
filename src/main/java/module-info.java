module org.example.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.tictactoe to javafx.fxml;
    exports org.example.tictactoe;
}