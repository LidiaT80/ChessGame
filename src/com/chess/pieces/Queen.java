package com.chess.pieces;

import com.chess.Coord;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Queen implements Piece {
    private int rank; //value for priority
    private String color;
    private Coord coord;
    private ImageIcon img;
    private int id;

    public Queen(String color, int x, int y,int id) {
        this.id = id;
        this.rank = 3;
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
      /*  if (color.equals("white")) {
        } else {
        }*/
      Rook rook = new Rook(getColor(),coord.x,coord.y,getId());
      Bishop bishop = new Bishop(getColor(),coord.x,coord.y,getId());

        List<List<Coord>> biRook= new ArrayList<>();
        biRook.addAll(rook.possibleMoves());
        biRook.addAll(bishop.possibleMoves());


        /*for (int i = 1; i <8 ; i++) {
            if ((coord.y+i)<7 && (coord.x+i)<7)
                coords.add(new Coord(coord.x+i,coord.y+i));
            if ((coord.x+i)<7 && (coord.y-i)>0)
                coords.add(new Coord(coord.x+i,coord.y-i));
            if ((coord.x-i)>0 && (coord.y-i)>0)
                coords.add(new Coord(coord.x-i,coord.y-i));
            if ((coord.x-i)>0 && (coord.y+i)<7)
                coords.add(new Coord(coord.x-i,coord.y+i));
            if ((coord.y+i)<7)
                coords.add(new Coord(coord.x,coord.y+i));
            if ((coord.y-i)>0)
                coords.add(new Coord(coord.x,coord.y-i));
            if ((coord.x+i)<7)
                coords.add(new Coord(coord.x+i,coord.y));
            if ((coord.x-i)>0)
                coords.add(new Coord(coord.x-i,coord.y));

        }*/
        return biRook;
    }

}
