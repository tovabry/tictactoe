package org.example.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model {
    private char[][] board;
    private char currentPlayer;
    public static final char PLAYER_X = 'X'; // Spelarens karaktär
    public static final char PLAYER_O = 'O'; // Datorns karaktär

    public Model() {
        initializeBoard();
    }

    public void initializeBoard() {
        board = new char[3][3];
        currentPlayer = PLAYER_X; // Spelaren börjar med 'X'
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(char currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isPlaceTaken(int row, int col) {
        return board[row][col] != '\0';
    }

    public boolean makeMove(int row, int col) {
        if (isPlaceTaken(row, col)) {
            return false;
        }
        board[row][col] = currentPlayer; // Placera den aktuella spelarens karaktär
        return true;
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
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        return board[0][2] == player && board[1][1] == player && board[2][0] == player;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    public int[] getComputerMove() {
        List<int[]> availableMoves = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '\0') {
                    availableMoves.add(new int[]{row, col});
                }
            }
        }
        if (availableMoves.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
}
