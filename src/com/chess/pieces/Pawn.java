package com.chess.pieces;

import com.chess.Coord;

import javax.swing.*;

public class Pawn implements Piece{

    private int val; //value for priority
    private String color;
    private Coord coord;
    private ImageIcon img;

    public Pawn(String color, int x,int y) {
        this.val = 1;
        this.color = color;
        this.coord = new Coord(x,y);

        if (color.equals("white")) {
            img = new ImageIcon("img/white_pawn1.png");
        } else //black
            img = new ImageIcon("img/black_pawn1.png");
    }

    @Override
    public Coord getPosition() {
        return coord;
    }

    public void setPosition(int x, int y) {
        coord.x=x;
        coord.y=y;
    }

    @Override
    public ImageIcon getImg() {
        return img;
    }

    @Override
    public int getValue() {
        return val;
    }
}
