package org.example.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model {
    private char[][] board;
    private char currentPlayer;

    public Model() {
        initializeBoard();
    }

    public void initializeBoard() {
        board = new char[3][3];
        currentPlayer = 'X'; // Spelaren börjar
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean makeMove(int row, int col) {
        if (board[row][col] == '\0') {
            board[row][col] = currentPlayer;
            return true;
        }
        return false;
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

    public boolean isGameWon() {
        // Kontrollerar rader, kolumner och diagonaler
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                return true;
            }
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                return true;
            }
        }
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }
        return false;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    // Enkel AI för datorn som väljer en slumpmässig ledig cell
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
            return null; // Ingen ledig plats kvar
        }

        Random random = new Random();
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
}
