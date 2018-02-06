package com.chess;

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

        //DemoMode
        Thread.sleep(1000);
        Coord coord = new Coord(p1.getPieces().get(5).getPosition().x + 2, p1.getPieces().get(5).getPosition().y);
        playerRandomMove();

    }

    void playerMove(Player player, int id, Coord destination) {
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
