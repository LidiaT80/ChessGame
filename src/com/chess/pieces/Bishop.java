package com.chess.pieces;

import com.chess.Coord;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Bishop implements Piece {
    private int rank; //value for priority
    private String color;
    private Coord coord;
    private ImageIcon img;
    private int id;

    public Bishop(String color, int x, int y, int id) {
        this.id = id;
        this.rank = 2;
        this.color = color;
        this.coord = new Coord(x, y);

        if (color.equals("white")) {
            img = new ImageIcon("img/white_bishop1.png");
        } else //black
            img = new ImageIcon("img/black_bishop1.png");
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
    public int getRank() {
        return rank;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getColor() {
        return color;
    }

    public List<List<Coord>> possibleMoves() {
        List<List<Coord>> coords = new ArrayList<>();
        int boardSize = 8;

        List<Coord> coord1 = new ArrayList<>();
        List<Coord> coord2 = new ArrayList<>();
        List<Coord> coord3 = new ArrayList<>();
        List<Coord> coord4 = new ArrayList<>();

      /*  if (color.equals("white")) {
        } else {
        }*/
        for (int i = 1; i < boardSize; i++) {
            if ((coord.y + i) < 7 && (coord.x + i) < 7)
                coord1.add(new Coord(coord.x + i, coord.y + i));
            if ((coord.x + i) < 7 && (coord.y - i) > 0)
                coord2.add(new Coord(coord.x + i, coord.y - i));
            if ((coord.x - i) > 0 && (coord.y - i) > 0)
                coord3.add(new Coord(coord.x - i, coord.y - i));
            if ((coord.x - i) > 0 && (coord.y + i) < 7)
                coord4.add(new Coord(coord.x - i, coord.y + i));
        }

        coords.add(coord1);
        coords.add(coord2);
        coords.add(coord3);
        coords.add(coord4);

        return coords;
    }

}
