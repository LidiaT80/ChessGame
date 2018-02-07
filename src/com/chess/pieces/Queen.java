package com.chess.pieces;

import com.chess.Coord;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Queen implements Piece {
    private int val; //value for priority
    private String color;
    private Coord coord;
    private ImageIcon img;
    private int id;

    public Queen(String color, int x, int y,int id) {
        this.id = id;
        this.val = 1;
        this.color = color;
        this.coord = new Coord(x, y);

        if (color.equals("white")) {
            img = new ImageIcon("img/white_queen1.png");
        } else //black
            img = new ImageIcon("img/black_queen1.png");
    }

    @Override
    public Coord getPosition() {
        return coord;
    }

    public void setPosition(int x, int y) {
        coord.x = x;
        coord.y = y;
    }

    @Override
    public ImageIcon getImg() {
        return img;
    }

    @Override
    public int getValue() {
        return val;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getColor() {
        return color;
    }

    public List<Coord> possibleMoves() {
        List<Coord> coords = new ArrayList<>();
        if (color.equals("white")) {
        } else {
        }
        return coords;
    }

    public List<Coord> killMove() {
        List<Coord> killCoords = new ArrayList<>();
        if (color.equals("white")) {
        } else {
        }
        return killCoords;
    }
}
