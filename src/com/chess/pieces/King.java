package com.chess.pieces;

import com.chess.Coord;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class King implements Piece {

    private int rank; //value for priority
    private String color;
    private Coord coord;
    private ImageIcon img;
    private int id;

    public King(String color, int x, int y, int id) {
        this.id = id;
        this.rank = 4;
        this.color = color;
        this.coord = new Coord(x, y);

        if (color.equals("white")) {
            img = new ImageIcon("img/white_king1.png");
        } else //black
            img = new ImageIcon("img/black_king1.png");
    }

    @Override
    public Coord getPosition() {
        return coord;
    }

    @Override
    public void setPosition(int x, int y) {
        coord.x=x;
        coord.y=y;
    }

    @Override
    public ImageIcon getImg() {
        return img;
    }

    @Override
    public List<Coord> possibleMoves() {
        List<Coord> coords = new ArrayList<>();
        return coords;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public int getRank() {
        return rank;
    }
}
