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

    public List<List<Coord>> possibleMoves() {
        List<List<Coord>> coords = new ArrayList<>();

        List<Coord> coord1 = new ArrayList<>();
        List<Coord> coord2 = new ArrayList<>();


        if (color.equals("white") && coord.y<7) {
            coord1.add(new Coord(coord.x,coord.y+1));
        } else if(color.equals("black") && coord.y>0){
            coord2.add(new Coord(coord.x,coord.y-1));
        }
        coords.add(coord1);
        coords.add(coord2);

        return coords;
    }

    public List<Coord> killMove() {
        List<Coord> killCoords = new ArrayList<>();
        if (color.equals("white")) {
            killCoords.add(new Coord(coord.x+1, coord.y+1));
            killCoords.add(new Coord(coord.x-1, coord.y+1));
        } else {
            killCoords.add(new Coord(coord.x+1, coord.y-1));
            killCoords.add(new Coord(coord.x-1, coord.y-1));
        }
        return killCoords;
    }
}
