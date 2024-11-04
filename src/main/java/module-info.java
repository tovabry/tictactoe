module org.example.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.tictactoe to javafx.fxml;
    exports org.example.tictactoe;
    exports org.example.tictactoe.socketo;
    opens org.example.tictactoe.socketo to javafx.fxml;
}