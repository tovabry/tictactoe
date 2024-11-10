package org.example.tictactoe;

import java.util.Objects;

public class Model {
    private char[][] board;
    private char currentPlayer;
    public static final char PLAYER_SERVER = ('O');
    public static final char PLAYER_CLIENT = ('X');

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

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isPlaceTaken(int row, int col) {
        return board[row][col] != '\0';
    }

    public boolean makeMove(int row, int col) {
        if (isPlaceTaken(row, col) || isGameWon(currentPlayer)) {
            return false;
        }

        board[row][col] = (currentPlayer == PLAYER_SERVER) ? 'X' : 'O';
        switchPlayer();
        return true;
    }

    public boolean receiveOpponentMove(int row, int col) {
        if (isWithinBounds(row, col) && !isPlaceTaken(row, col)) {
            board[row][col] = (Objects.equals(currentPlayer, PLAYER_SERVER)) ? 'O' : 'X';
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

    public boolean isGameWon(char player) {
        char symbol = (player == PLAYER_SERVER) ? 'X' : 'O'; //om spelare är server tilldelas variabeln symbol X, annars O
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol)
                return true;
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)
                return true;
        }

        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) return true;
        return board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_SERVER) ? PLAYER_CLIENT : PLAYER_SERVER; //om currentplayer är server switchas turen till klient, annars tvärtom
    }

}

