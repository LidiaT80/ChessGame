package com.chess;

import com.chess.pieces.Piece;
import sun.awt.SunHints;

import javax.swing.*;
import java.awt.*;
import java.security.Key;
import java.util.Map;
import java.util.stream.Stream;

public class Board extends JPanel {

    JPanel[][] fields = new JPanel[8][8];

/*  Flyttar in dessa till piece klasserna
    ImageIcon blackRook = new ImageIcon("img/black_rook1.png");
    ImageIcon blackKnight = new ImageIcon("img/black_knight1.png");
    ImageIcon blackBishop = new ImageIcon("img/black_bishop1.png");
    ImageIcon blackQueen = new ImageIcon("img/black_queen1.png");
    ImageIcon blackKing = new ImageIcon("img/black_king1.png");
    ImageIcon blackPawn = new ImageIcon("img/black_pawn1.png");
    ImageIcon whitePawn = new ImageIcon("img/white_pawn1.png");
    ImageIcon whiteRook = new ImageIcon("img/white_rook1.png");
    ImageIcon whiteKnight = new ImageIcon("img/white_knight1.png");
    ImageIcon whiteBishop = new ImageIcon("img/white_bishop1.png");
    ImageIcon whiteQueen = new ImageIcon("img/white_queen1.png");
    ImageIcon whiteKing = new ImageIcon("img/white_king1.png");
 */

    public Board() {
        setLayout(new GridLayout(8, 8));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        setBackground(Color.BLACK);
        setSize(600, 600);
    }

    public void createBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
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

    public void movePiece(Player player, int id, Coord coord) {    //Här har jag bara testat att flytta på en pjäs
        int x = player.getPieces().get(id).getPosition().x;
        int y = player.getPieces().get(id).getPosition().y;
        int newx = coord.x;
        int newy = coord.y;
        Component comp = fields[x][y].getComponent(0);   //hämtar component på current destination

        fields[x][y].remove(comp);                         //rensar components på current destination
        if (fields[x][y].getComponents() != null)          //kollar om det finns något på destinationen
            fields[newx][newy].remove(comp);               //rensar destinationen
        fields[newx][newy].add(new JLabel(player.getPieces().get(id).getImg())); //lägger till img på ny destination
        fields[x][y].revalidate();
        fields[newx][newy].revalidate();
        fields[x][y].repaint();
        fields[newx][newy].repaint();

        player.getPieces().get(id).setPosition(newx,newy);
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
}
