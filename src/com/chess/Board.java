package com.chess;

import com.chess.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.Map;


public class Board extends JPanel {

    JPanel[][] fields = new JPanel[8][8];

    public Board() {
        setLayout(new GridLayout(8, 8));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        setBackground(Color.BLACK);
        setSize(600, 600);
    }

    public void createBoard() {
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                fields[i][j] = new JPanel();
                fields[i][j].setLayout(new FlowLayout());
                if ((i + j) % 2 == 0)
                    fields[i][j].setBackground(Color.LIGHT_GRAY);
                else
                    fields[i][j].setBackground(Color.GRAY);

                fields[i][j].revalidate();
                fields[i][j].repaint();
                add(fields[i][j]);
            }
        }
    }

    public void movePiece(Player player,Player opponent, int id, Coord coord) {    //Här har jag bara testat att flytta på en pjäs
        int x = player.getPieces().get(id).getPosition().x;
        int y = player.getPieces().get(id).getPosition().y;
        int newx = coord.x;
        int newy = coord.y;
        Component comp = fields[x][y].getComponent(0);   //hämtar component på current destination

        fields[x][y].remove(comp);//rensar components på current destination
        if (fields[newx][newy].getComponents().length != 0) //kollar om det finns något på destinationen
        {
            fields[newx][newy].remove(0);               //rensar destinationen
            opponent.removePiece(new Coord(newx,newy));
        }

        fields[newx][newy].add(new JLabel(player.getPieces().get(id).getImg())); //lägger till img på ny destination
        fields[x][y].revalidate();
        fields[newx][newy].revalidate();
        fields[x][y].repaint();
        fields[newx][newy].repaint();

        player.getPieces().get(id).setPosition(newx, newy);
    }


    public void initPlayers(Map<Integer, Piece> p1Pieces, Map<Integer, Piece> p2Pieces) {
        for (Piece p : p1Pieces.values()) {
            fields[p.getPosition().x][p.getPosition().y].add(new JLabel(p.getImg()));
            fields[p.getPosition().x][p.getPosition().y].revalidate();
            fields[p.getPosition().x][p.getPosition().y].repaint();
        }
        for (Piece p : p2Pieces.values()) {
            fields[p.getPosition().x][p.getPosition().y].add(new JLabel(p.getImg()));
            fields[p.getPosition().x][p.getPosition().y].revalidate();
            fields[p.getPosition().x][p.getPosition().y].repaint();
        }
    }

    public Piece checkPosition(Coord coord, Player p1, Player p2) {
        try {
            for (Piece piece:p1.getPieces().values()) {

               if(piece.getPosition().x==coord.x && piece.getPosition().y==coord.y)

               // if(fields[coord.x][coord.y].getComponent(0).toString().contains(piece.getImg().toString()))
                    return piece;
            }
            for (Piece piece:p2.getPieces().values()) {

                if(piece.getPosition().x==coord.x && piece.getPosition().y==coord.y)
                //if(fields[coord.x][coord.y].getComponent(0).toString().contains(piece.getImg().toString()))
                    return piece;
            }


        } catch (Exception e) {
            return null;
        }
        return null;
    }
}

