package org.example.tictactoe;

public class Model {
    private char[][] board;
    private char currentPlayer;
    public static final char PLAYER_X = 'X';
    public static final char PLAYER_O = 'O';

    public Model() {
        initializeBoard();
    }

    public void initializeBoard() {
        board = new char[3][3];
        currentPlayer = PLAYER_X;
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

        board[row][col] = currentPlayer;
        switchPlayer();
        return true;
    }

    public boolean receiveOpponentMove(int row, int col) {
        if (isWithinBounds(row, col) && !isPlaceTaken(row, col)) {
            board[row][col] = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X; // Alternativ spelare
            // Byt spelare efter att motståndarens drag är mottaget
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
