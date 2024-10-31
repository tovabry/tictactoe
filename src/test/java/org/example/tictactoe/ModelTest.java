package org.example.tictactoe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ModelTest {
    //Koden som testar för giltiga drag och om rundan är färdigspelad ska ha tester
    @BeforeEach
    void setUp() {
        model = new Model();
    }

    Model model = new Model();


@Test
void testValidMove() {
    assertTrue(model.makeMove(0, 0), "Moves on an empty place should be invalid");
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
    assertTrue(model.isPlaceTaken(2,1), "Place should be occupied after already taken");

}




}