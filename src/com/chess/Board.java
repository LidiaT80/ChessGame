package com.chess;

import com.chess.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Board extends JPanel {

    JPanel[][] fields = new JPanel[8][8];

    public Board() {
        setLayout(new GridLayout(8, 8));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        setBackground(Color.BLACK);
        setSize(600, 600);
    }

    public void createBoard() {

        IntStream.range(0,8).forEach(y -> IntStream.range(0,8)
                .forEach(x ->{fields[x][y]= new JPanel();
                    fields[x][y].setLayout(new FlowLayout());
                    if ((x + y) % 2 == 0)
                        fields[x][y].setBackground(Color.LIGHT_GRAY);
                    else
                        fields[x][y].setBackground(Color.GRAY);

                    fields[x][y].revalidate();
                    fields[x][y].repaint();
                    add(fields[x][y]);}
                ));
    }


    public void movePiece(Player player,Player opponent, int id, Coord coord) {

        int x = player.getPieces().get(id).getPosition().x;
        int y = player.getPieces().get(id).getPosition().y;
        int newx = coord.x;
        int newy = coord.y;
        Component comp = fields[x][y].getComponent(0);   //hämtar component på current destination

        fields[x][y].remove(comp);//rensar components på current destination
        if (fields[newx][newy].getComponents().length != 0) //kollar om det finns något på destinationen
        {

            fields[newx][newy].remove(0);               //rensar destinationen
            opponent.removePiece(coord);

        }

        fields[newx][newy].add(new JLabel(player.getPieces().get(id).getImg())); //lägger till img på ny destination
        fields[x][y].revalidate();
        fields[newx][newy].revalidate();
        fields[x][y].repaint();
        fields[newx][newy].repaint();

        player.getPieces().get(id).setPosition(newx, newy);
    }


    public void initPlayers(Map<Integer, Piece> p1Pieces, Map<Integer, Piece> p2Pieces) {

        Stream<Piece> stream=Stream.concat(p1Pieces.values().stream(), p2Pieces.values().stream());

        stream.forEach(p -> {fields[p.getPosition().x][p.getPosition().y].add(new JLabel(p.getImg()));
                             fields[p.getPosition().x][p.getPosition().y].revalidate();
                             fields[p.getPosition().x][p.getPosition().y].repaint();});

    }

    public Piece checkPosition(Coord coord, Player p1, Player p2) {

        Stream<Piece> stream=Stream.concat(p1.getPieces().values().stream(), p2.getPieces().values().stream());
        Stream<Piece> stream1=Stream.concat(p1.getPieces().values().stream(), p2.getPieces().values().stream());

        if(stream.filter(p -> p.getPosition().x==coord.x).anyMatch(p -> p.getPosition().y==coord.y)){

            return stream1
                    .filter(p -> p.getPosition().x==coord.x && p.getPosition().y==coord.y)
                    .findFirst()
                    .get();
        }

        return null;
    }
}

