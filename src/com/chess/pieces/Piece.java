package com.chess.pieces;

import com.chess.Coord;

import javax.swing.*;
import java.util.List;

public interface Piece {
    Coord getPosition();
    void setPosition(int x,int y);
    int getValue();
    ImageIcon getImg();
    List<Coord> killMove();
}
