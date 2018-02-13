package com.chess.pieces;

import com.chess.Coord;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Rook implements Piece {
    private int rank; //value for priority
    private String color;
    private Coord coord;
    private ImageIcon img;
    private int id;

    public Rook(String color, int x,int y, int id) {
        this.id = id;
        this.rank = 2;
        this.color = color;
        this.coord = new Coord(x,y);

        if (color.equals("white")) {
            img = new ImageIcon("img/white_rook1.png");
        } else //black
            img = new ImageIcon("img/black_rook1.png");
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
    public List<Coord> possibleMoves() {
        List<Coord> coords = new ArrayList<>();
        int boardSize = 8;
/*
        for(int x = 0;x<boardSize;x++){
            if(!(coord.x == x)) //Om inte på current x-position
            coords.add(new Coord(x,coord.y));
        }
        for(int y = 0;y<boardSize;y++){
            if(!(coord.y == y)) //Om inte på current y-position
                coords.add(new Coord(coord.x,y));
        }*/

        for (int i = 1; i <boardSize ; i++) {
            if ((coord.y+i)<7)
                coords.add(new Coord(coord.x,coord.y+i));
            if ((coord.y-i)>0)
                coords.add(new Coord(coord.x,coord.y-i));
            if ((coord.x+i)<7)
                coords.add(new Coord(coord.x+i,coord.y));
            if ((coord.x-i)>0)
                coords.add(new Coord(coord.x-i,coord.y));
        }
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
