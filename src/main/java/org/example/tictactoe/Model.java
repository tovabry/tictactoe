package org.example.tictactoe;

import java.util.Objects;

public class Model {
    private char[][] board;
    private String currentPlayer;
    public static final String PLAYER_SERVER = "Server";
    public static final String PLAYER_CLIENT = "Client";

    public Model() {
        initializeBoard();
    }

    public void initializeBoard() {
        board = new char[3][3];
        currentPlayer = PLAYER_SERVER;
    }

    public char[][] getBoard() {
        char[][] boardCopy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, 3);
        }
        return boardCopy;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isPlaceTaken(int row, int col) {
        return board[row][col] != '\0';
    }

    public boolean makeMove(int row, int col) {
        if (isPlaceTaken(row, col) || isGameWon(currentPlayer)) {
            return false;
        }

        board[row][col] = (currentPlayer == PLAYER_SERVER) ? 'X' : 'O'; // Använd X eller O baserat på spelare
        switchPlayer();
        return true;
    }

    public boolean receiveOpponentMove(int row, int col) {
        if (isWithinBounds(row, col) && !isPlaceTaken(row, col)) {
            board[row][col] = (Objects.equals(currentPlayer, PLAYER_SERVER)) ? 'O' : 'X'; // Alternativ spelare
            switchPlayer();
            return true;
        }
        return false;
    }

    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }

    public boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGameWon(String player) {
        char symbol = (player.equals(PLAYER_SERVER)) ? 'X' : 'O'; // Använd X eller O baserat på spelare
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) return true;
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) return true;
        }
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) return true;
        return board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer.equals(PLAYER_SERVER)) ? PLAYER_CLIENT : PLAYER_SERVER; // Byt spelare
    }

//    public int[] getComputerMove() {
//        List<int[]> availableMoves = new ArrayList<>();
//        for (int row = 0; row < 3; row++) {
//            for (int col = 0; col < 3; col++) {
//                if (board[row][col] == '\0') {
//                    availableMoves.add(new int[]{row, col});
//                }
//            }
//        }
//        if (availableMoves.isEmpty()) {
//            return null;
//        }
//        Random random = new Random();
//        return availableMoves.get(random.nextInt(availableMoves.size()));
//    }
}
