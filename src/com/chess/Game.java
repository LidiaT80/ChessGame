package com.chess;

import com.chess.pieces.Piece;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Game {
    private Player p1;
    private Player p2;
    private Board board;

    Game(Board board) throws InterruptedException {
        p1 = new Player("white");
        p2 = new Player("black");
        this.board = board;
        board.initPlayers(p1.getPieces(), p2.getPieces());
        boolean game = true;
        boolean run = true;
        System.out.println(p1.getPieces().get(1).getImg().getDescription());
        while (game) {
            chooseMove(p1);
            chooseMove(p2);
        }


    }

    private void demo() throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
            int rnd = ThreadLocalRandom.current().nextInt(0, 7);
            int x = p1.getPieces().get(rnd).getPosition().x;
            int y = p1.getPieces().get(rnd).getPosition().y + 1;
            Coord destination = new Coord(x, y);
            move(p1, rnd, destination);

            Thread.sleep(500);

            rnd = ThreadLocalRandom.current().nextInt(0, 7);
            x = p2.getPieces().get(rnd).getPosition().x;
            y = p2.getPieces().get(rnd).getPosition().y - 1;
            destination = new Coord(x, y);
            board.movePiece(p2, rnd, destination);
        }
    }

    void move(Player player, int id, Coord destination) {
        board.movePiece(player, id, destination);
    }

    public Map<Integer, List<Coord>> canMove(Map<Integer, Piece> pieces) {
        Map<Integer, List<Coord>> movablePieces = new HashMap<>();
        Map<Integer, List<Coord>> killingPieces = new HashMap<>();

        for (Piece p : pieces.values()) {
            List<Coord> movableCoords = new ArrayList<>();
            List<Coord> killingCoords = new ArrayList<>();
            for (Coord coord : p.possibleMoves()) {
                Piece piece = board.checkPosition(coord, p1, p2);
                if(!(piece==null) && !(piece.getColor().equals(p.getColor()))){
                    killingCoords.add(coord);
                }
                if(piece==null || !(piece.getColor().equals(p.getColor()))){
                    movableCoords.add(coord);
                }
            }
            movablePieces.put(p.getId(), movableCoords);
            if(killingCoords.size()!=0)
                killingPieces.put(p.getId(),killingCoords);
        }

        if (!killingPieces.isEmpty())
            return killingPieces;
        else
            return movablePieces;
    }


    public void chooseMove(Player p) {
        Map<Integer, List<Coord>> movables = new HashMap<>(canMove(p.getPieces()));
        int id,r;
        do{
            id=ThreadLocalRandom.current().nextInt(0,15);
        }while (!(movables.containsKey(id)));

        List<Coord> coordList=movables.get(id);
        
        r=ThreadLocalRandom.current().nextInt(0,coordList.size()-1);
        move(p,id,coordList.get(r));


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
      TODO fungerande pawns!
      TODO hantering när pjäs tar en pjäs
      TODO ett komplett game med pawns + kung
      TODO tweak AI
    */
}
