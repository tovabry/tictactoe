package org.example.tictactoe;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ModelTest {
    @BeforeEach
    void setUp() {
        model = new Model();
    }

    Model model = new Model();


@Test
void testValidMove() {
    assertTrue(model.makeMove(0, 0), "Moves on an empty place should be valid");
    assertEquals(Model.PLAYER_X, model.getBoard()[0][0]  , "Place should contain 'X' after a move");
}

@Test
void testInvalidMoveOutOfBounds() {
    assertFalse(model.isWithinBounds(3,3), "Move outside boards limits should be invalid");
    assertFalse(model.isWithinBounds(-1, 0), "Moves with negative coordinates should be invalid");
}

@Test
@DisplayName("Test invalid move on occupied place")
void testInvalidMoveOnOccupiedPlace(){
    model.makeMove(2, 1);
    model.switchPlayer();
    model.makeMove(2, 1);
    assertEquals(Model.PLAYER_X, model.getBoard()[2][1]);
    assertTrue(model.isPlaceTaken(2,1), "Place should be occupied after already taken");
}

@Test
@DisplayName("Test if player wins after three in a row")
void testIfPlayerWinsAfterThreeInARow() {
    char player = 'X';
    model.makeMove(0, 0);
    model.makeMove(0, 1);
    model.makeMove(0, 2);
    assertTrue(model.isGameWon(player));
}

@Test
@DisplayName("Test if player can make a move after game is won")
void testIfPlayerCanMakeAMoveAfterGameIsWon() {
    model.makeMove(0, 0);
    model.makeMove(0, 1);
    model.makeMove(0, 2);

    assertFalse(model.makeMove(2, 2), "Player should not be able to make a move after game is won");
}

    @Test
    @DisplayName("Test that computer move follows turn order")
    void testThatComputerMoveFollowsTurnOrder() {
        model.makeMove(0, 0);
        model.switchPlayer();
        assertEquals(Model.PLAYER_O, model.getCurrentPlayer(), "Current player should switch to O after X's move");

        int[] computerMove = model.getComputerMove();
        model.makeMove(computerMove[0], computerMove[1]);
        model.switchPlayer();
        assertEquals(Model.PLAYER_X, model.getCurrentPlayer(), "Current player should switch back to X after O's move");
    }

    @Test
    @DisplayName("Test for draw when board is full")
    void testForDrawWhenBoardIsFull() {
        model.makeMove(0, 0); model.switchPlayer();
        model.makeMove(0, 1); model.switchPlayer();
        model.makeMove(0, 2); model.switchPlayer();
        model.makeMove(1, 1); model.switchPlayer();
        model.makeMove(1, 0); model.switchPlayer();
        model.makeMove(2, 0); model.switchPlayer();
        model.makeMove(1, 2); model.switchPlayer();
        model.makeMove(2, 2); model.switchPlayer();
        model.makeMove(2, 1);

        assertTrue(model.isBoardFull(), "Board should be full");
        assertFalse(model.isGameWon(Model.PLAYER_X) || model.isGameWon(Model.PLAYER_O), "There should be no winner");
    }


}