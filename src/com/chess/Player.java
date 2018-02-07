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
                pieces.put(id, new Pawn("white", i, 1,id));
            }
            id++;
            pieces.put(id, new Rook("white", 0, 0,id));
            id++;
            pieces.put(id, new Bishop("white", 1, 0,id));
            id++;
            pieces.put(id, new Knight("white", 2, 0,id));
            id++;
            pieces.put(id, new King("white", 3, 0,id));
            id++;
            pieces.put(id, new Queen("white", 4, 0,id));
            id++;
            pieces.put(id, new Knight("white", 5, 0,id));
            id++;
            pieces.put(id, new Bishop("white", 6, 0,id));
            id++;
            pieces.put(id, new Rook("white", 7, 0,id));



        } else { //black
            for (int i = 0; i < 8; i++,id++) {
                pieces.put(id, new Pawn("black", i, 6,id));
            }
            id++;
            pieces.put(id, new Rook("black", 0, 7,id));
            id++;
            pieces.put(id, new Bishop("black", 1, 7,id));
            id++;
            pieces.put(id, new Knight("black", 2, 7,id));
            id++;
            pieces.put(id, new King("black", 3, 7,id));
            id++;
            pieces.put(id, new Queen("black", 4, 7,id));
            id++;
            pieces.put(id, new Knight("black", 5, 7,id));
            id++;
            pieces.put(id, new Bishop("black", 6, 7,id));
            id++;
            pieces.put(id, new Rook("black", 7, 7,id));
        }

    }


    public Map<Integer, Piece> getPieces() {
        return pieces;
    }
}
