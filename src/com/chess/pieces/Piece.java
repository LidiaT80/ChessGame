package com.chess.pieces;

import com.chess.Coord;

import javax.swing.*;

public interface Piece {
    Coord getPosition();
    void setPosition(int x,int y);
    int getValue();
    ImageIcon getImg();

}
