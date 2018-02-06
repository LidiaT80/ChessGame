package com.chess;

import com.chess.pieces.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            chooseMove(p1.getPieces());
            chooseMove(p2.getPieces());
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

        for (Piece p : pieces.values()) {
            List<Coord> coords = new ArrayList<>();
            for (Coord coord : p.possibleMoves()) {
                Piece piece = board.checkPosition(coord, p1, p2);

                if (p.getImg().getDescription().contains("pawn")) {
                    if (!(piece == null)) {
                        coords.add(coord);
                        movablePieces.put(p.getId(), coords);
                    }
                }
            }
        }
        //TODO kolla om vägen till destination är tom
        return movablePieces;
    }


    public Map<Integer, List<Coord>> getPossibleKills(Map<Integer, List<Coord>> mapList) {
        Map<Integer, List<Coord>> killingPieces = new HashMap<>();

//        if(!(piece.getColor().equals(p.getColor()))){
//            piece.getId();
//        }

        /*ny lista med Pieces
        kolla canMove listan om pjäsen kan ta en motståndarpjäs och lägg till den pjäsen i listan
        */

        return killingPieces;
    }

    public void chooseMove(Map<Integer, Piece> pieces) {
        Map<Integer, List<Coord>> movables = new HashMap<>(canMove(pieces));
        getPossibleKills(movables);
        //getPossibleKills();
        //randomize
        //move();

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
