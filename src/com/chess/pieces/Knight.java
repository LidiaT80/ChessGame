package com.chess.pieces;

import com.chess.Coord;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Knight implements Piece {
    private int rank; //value for priority
    private String color;
    private Coord coord;
    private ImageIcon img;
    private int id;

    public Knight(String color, int x, int y,int id) {
        this.id = id;
        this.rank = 2;
        this.color = color;
        this.coord = new Coord(x, y);

        if (color.equals("white")) {
            img = new ImageIcon("img/white_knight1.png");
        } else //black
            img = new ImageIcon("img/black_knight1.png");
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
        if(coord.x<7){
            if(coord.y>1)
                coords.add(new Coord(coord.x+1,coord.y-2));
            if(coord.y<6)
                coords.add(new Coord(coord.x+1,coord.y+2));
        }
        if(coord.x<6){
            if(coord.y>0)
                coords.add(new Coord(coord.x+2,coord.y-1));
            if(coord.y<7)
                coords.add(new Coord(coord.x+2,coord.y+1));
        }
        if(coord.x>0){
            if(coord.y>1)
                coords.add(new Coord(coord.x-1,coord.y-2));
            if(coord.y<6)
                coords.add(new Coord(coord.x-1,coord.y+2));
        }
        if(coord.x>1){
            if(coord.y>0)
                coords.add(new Coord(coord.x-2,coord.y-1));
            if(coord.y<7)
                coords.add(new Coord(coord.x-2,coord.y+1));
        }
        return coords;
    }

}
