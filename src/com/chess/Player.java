package com.chess;

import com.chess.pieces.*;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String color;
    private Map<Integer, Piece> pieces = new HashMap<>();

    public Player(String color) {
        this.color = color;
        int id = 0;

        if (color.equals("white")) {
            for (int i = 0; i < 8; i++,id++) {
                pieces.put(id, new Pawn("white", 1, i,id));
            }
            id++;
            pieces.put(id, new King("white", 0, 3,id));


        } else { //black
            for (int i = 0; i < 8; i++,id++) {
                pieces.put(id, new Pawn("black", 6, i,id));
            }
            id++;
            pieces.put(id, new King("black", 7, 3,id));
        }

    }


    public Map<Integer, Piece> getPieces() {
        return pieces;
    }
}
