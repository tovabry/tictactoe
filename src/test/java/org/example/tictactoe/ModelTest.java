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
    assertEquals(Model.PLAYER_SERVER, model.getBoard()[0][0], "Place should contain 'O' after a move");
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
    assertTrue(model.isPlaceTaken(2,1), "Place should be occupied after already taken");

}

@Test
@DisplayName("Test if player wins after three in a row")
void testIfPlayerWinsAfterThreeInARow() {
    char player = 'O';
    model.makeMove(0, 0);
    model.switchPlayer();
    model.makeMove(0, 1);
    model.switchPlayer();
    model.makeMove(0, 2);
    assertTrue(model.isGameWon(player));
}

@Test
@DisplayName("Test if player can make a move after game is won")
void testIfPlayerCanMakeAMoveAfterGameIsWon() {
    model.makeMove(0, 0);
    model.switchPlayer();
    model.makeMove(0, 1);
    model.switchPlayer();
    model.makeMove(0, 2);
    model.switchPlayer();

    assertFalse(model.makeMove(2, 2), "Player should not be able to make a move after game is won");
}
//enum
//open/closeprincipen

}