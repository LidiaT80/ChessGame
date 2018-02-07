package com.chess;

import com.chess.pieces.Piece;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Game {
    Player p1;
    Player p2;
    Board board;

    Game(Board board) throws InterruptedException {
        p1 = new Player("white");
        p2 = new Player("black");
        this.board = board;
        board.initPlayers(p1.getPieces(), p2.getPieces());
        boolean game = true;
        System.out.println(p1.getPieces().get(1).getImg().getDescription());
        while (game) {
            chooseMove(p1);
            chooseMove(p2);
        }
        //DemoMode
        Thread.sleep(1000);
        Coord coord = new Coord(p1.getPieces().get(5).getPosition().x + 2, p1.getPieces().get(5).getPosition().y);
        playerRandomMove();

    }

    void move(Player player, int id, Coord destination) {
        board.movePiece(player, id, destination);
    }

    void playerRandomMove() throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
            int rnd = ThreadLocalRandom.current().nextInt(0, 8);
            int x = p1.getPieces().get(rnd).getPosition().x + 1;
            int y = p1.getPieces().get(rnd).getPosition().y;
            Coord destination = new Coord(x, y);
            board.movePiece(p1, rnd, destination);

            Thread.sleep(500);

            rnd = ThreadLocalRandom.current().nextInt(0, 8);
            x = p2.getPieces().get(rnd).getPosition().x - 1;
            y = p2.getPieces().get(rnd).getPosition().y;
            destination = new Coord(x, y);
            board.movePiece(p2, rnd, destination);
        }
    }

    public Map<Integer, List<Coord>> canMove(Map<Integer, Piece> pieces) {
        Map<Integer, List<Coord>> movablePieces = new HashMap<>();
        Map<Integer, List<Coord>> killingPieces = new HashMap<>();

        for (Piece p : pieces.values()) {
            List<Coord> movableCoords = new ArrayList<>();
            List<Coord> killingCoords = new ArrayList<>();
            for (Coord coord : p.possibleMoves()) {
                Piece piece = board.checkPosition(coord, p1, p2);
                if(piece!=null && !(piece.getColor().equals(p.getColor()))){
                    killingCoords.add(coord);
                }
                if(piece==null || !(piece.getColor().equals(p.getColor()))){
                    movableCoords.add(coord);
                }
            }
            movablePieces.put(p.getId(), movableCoords);
            killingPieces.put(p.getId(),killingCoords);
        }

        if (!killingPieces.isEmpty())
            return killingPieces;
        else
            return movablePieces;
    }


 /*   public Map<Integer, List<Coord>> getPossibleKills(Map<Integer, Piece> pieces) {


        for (Piece p:pieces.values()) {

            for (Coord coord : p.possibleMoves()) {
                Piece piece = board.checkPosition(coord, p1, p2);

            }

        }
        return ;
    }*/

    public void chooseMove(Player p) {
        Map<Integer, List<Coord>> movables = new HashMap<>(canMove(p.getPieces()));
        int r;
        do{
            r=ThreadLocalRandom.current().nextInt(0,15);
        }while (!(movables.containsKey(r)));

        List<Coord> coordList=movables.get(r);
        r=ThreadLocalRandom.current().nextInt(0,coordList.size());
        move(p,r,coordList.get(r));

        //getPossibleKills();
        //randomize
        //move();
        //TODO kolla om vägen till destination är tom
    /*kolla om getPossibleKills isåfall använda getPossibleMoves listan
      om där är kills, kolla prio, randomize bland dom högsta prio eller getPossibleMoves listan om ingen kills.
      sen flytta
    */
    }

    /*
      TODO göra klasser till alla pjäser
      TODO skriva ut alla pjäserna på rätt plats i Player klassen
      TODO fungerande pawns!
      TODO leta efter possible targets
      TODO random pjäs som KAN flytta ska flytta
      TODO while loop där spelarna gör vars ett move
      TODO hantering när pjäs tar en pjäs
      TODO ett komplett game med pawns + kung
      TODO logik för hur pjäserna får flytta
      TODO AI
    */
}
