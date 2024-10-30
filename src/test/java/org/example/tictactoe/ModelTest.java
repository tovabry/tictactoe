package org.example.tictactoe;

import static org.junit.jupiter.api.Assertions.*;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ModelTest {

    Model model = new Model();
//Koden som testar för giltiga drag och om rundan är färdigspelad ska ha tester
    @Test
    void hej(){
        model.makeMove(1, 2);


    }

}