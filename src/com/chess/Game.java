package com.chess;

import com.chess.pieces.Pawn;
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
        while (game) {

            Thread.sleep(2000);
            chooseMove(p1, p2);
            Thread.sleep(2000);
            chooseMove(p2, p1);

        }


    }


    void move(Player player, Player opponent, int id, Coord destination) {
        board.movePiece(player, opponent, id, destination);
    }


    public Map<Integer, List<Coord>> canMove(Map<Integer, Piece> pieces) {
        Map<Integer, List<Coord>> movablePieces = new HashMap<>();
        Map<Integer, List<Coord>> killingPieces = new HashMap<>();

        for (Piece piece : pieces.values()) {
            List<Coord> movableCoords = new ArrayList<>();
            List<Coord> killingCoords = new ArrayList<>();
            for (Coord coord : piece.possibleMoves()) {
                Piece targetPiece = board.checkPosition(coord, p1, p2);

                // EGEN KOD FÖR PAWN
                if (piece.getImg().getDescription().contains("pawn")) {
                    if (targetPiece == null) {
                        movableCoords.add(coord);
                    }
                    Pawn pawn = (Pawn) piece;
                    for (Coord pawnCoord : pawn.killMove()) {
                        targetPiece = board.checkPosition(pawnCoord, p1, p2);
                        if (!(targetPiece == null) && !(targetPiece.getColor().equals(piece.getColor()))) { //Om motståndare finns på rutan
                            killingCoords.add(pawnCoord);
                        }
                    }
                } else {
                    // EGEN KOD FÖR PAWN /END


                    if (!(targetPiece == null) && !(targetPiece.getColor().equals(piece.getColor()))) { //Om motståndare finns på rutan
                        killingCoords.add(coord);
                    }
                    if (targetPiece == null) {
                        movableCoords.add(coord);
                    }
                }
            }
            if (movableCoords.size() != 0)
                movablePieces.put(piece.getId(), movableCoords);
            if (killingCoords.size() != 0)
                killingPieces.put(piece.getId(), killingCoords);
        }

        if (!killingPieces.isEmpty())
            return killingPieces;
        else
            return movablePieces;

        //TODO pawns, egen logik
    }



    public void chooseMove(Player player, Player opponent) {
        Map<Integer, List<Coord>> movables = new HashMap<>(canMove(player.getPieces()));
        int randomIDpick, randomCoordPick;

        do {
            randomIDpick = ThreadLocalRandom.current().nextInt(0, 16);
        } while (!(movables.containsKey(randomIDpick)));

        List<Coord> coordList = movables.get(randomIDpick);
        if (coordList.size() < 1) {
            randomCoordPick = ThreadLocalRandom.current().nextInt(0, coordList.size() - 1);
        } else {
            randomCoordPick = 0;
        }
        move(player, opponent, randomIDpick, coordList.get(randomCoordPick));



        //TODO kolla om vägen till destination är tom

    }
}
