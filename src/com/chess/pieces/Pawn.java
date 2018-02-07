package com.chess.pieces;

import com.chess.Coord;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn implements Piece {

    private int rank; //value for priority
    private String color;
    private Coord coord;
    private ImageIcon img;
    private int id;

    public Pawn(String color, int x, int y,int id) {
        this.id = id;
        this.rank = 1;
        this.color = color;
        this.coord = new Coord(x, y);

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

    public List<Coord> possibleMoves() {
        List<Coord> coords = new ArrayList<>();
        if (color.equals("white")) {
            coords.add(new Coord(coord.x++,coord.y));
        } else {
           coords.add(new Coord(coord.x--,coord.y));
        }
        return coords;
    }

    public List<Coord> killMove() {
        List<Coord> killCoords = new ArrayList<>();
        if (color.equals("white")) {
            killCoords.add(new Coord(coord.x++, coord.y++));
            killCoords.add(new Coord(coord.x++, coord.y--));
        } else {
            killCoords.add(new Coord(coord.x--, coord.y++));
            killCoords.add(new Coord(coord.x--, coord.y--));
        }
        return killCoords;
    }
}
