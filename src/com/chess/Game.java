package com.chess;

import com.chess.pieces.Knight;
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
        boolean game1 = true;
        boolean game2 = true;
        while (game1 && game2) {

            Thread.sleep(2000);
            game1 = chooseMove(p1, p2);
            if (!game1)
                break;
            Thread.sleep(2000);
            game2 = chooseMove(p2, p1);

        }


    }


    boolean move(Player player, Player opponent, int id, Coord destination) {
        return board.movePiece(player, opponent, id, destination);
    }


    public Map<Integer, List<Coord>> canMove(Map<Integer, Piece> pieces) {
        Map<Integer, Map<Integer, List<Coord>>> movablePieces = new HashMap<>();

        for (Piece piece : pieces.values()) {
            Map<Integer, List<Coord>> rankedCoordList = new HashMap<>();
            for (List<Coord> coordList : piece.possibleMoves()) {
                for (Coord coord : coordList) {
                    Piece targetPiece = board.checkPosition(coord, p1, p2);

                    // EGEN KOD FÖR PAWN
                    if (piece instanceof Pawn) {
                        if (targetPiece == null)
                            rankedCoordList.computeIfAbsent(0, k -> new ArrayList<>()).add(coord); //lägg till coord i lista, skapa ny om lista ej finns

                        Pawn pawn = (Pawn) piece;
                        for (Coord pawnCoord : pawn.killMove()) {
                            targetPiece = board.checkPosition(pawnCoord, p1, p2);
                            if (!(targetPiece == null) && !(targetPiece.getColor().equals(piece.getColor())))  //Om motståndare finns på rutan
                                rankedCoordList.computeIfAbsent(targetPiece.getRank(), k -> new ArrayList<>()).add(coord); //lägg till coord i lista, skapa ny om lista ej finns
                        }
                    } else {
                        // EGEN KOD FÖR PAWN /END


                        if (!(targetPiece == null) && targetPiece.getColor().equals(piece.getColor()) && !(piece instanceof Knight))
                            break;         //Om friendly pjäs på rutan, gå till nästa lista
                        else if (!(targetPiece == null) && !(targetPiece.getColor().equals(piece.getColor())))  //Om motståndare finns på rutan
                            rankedCoordList.computeIfAbsent(targetPiece.getRank(), k -> new ArrayList<>()).add(coord); //lägg till coord i lista, skapa ny om lista ej finns
                        else if (targetPiece == null)                                         //om rutan är tom
                            rankedCoordList.computeIfAbsent(0, k -> new ArrayList<>()).add(coord); //lägg till coord i lista, skapa ny om lista ej finns

                    }
                }
            }
            movablePieces.put(piece.getId(), rankedCoordList);
        }
        //TODO filtrera ut coords med högst rank movablePieces->rankedCoordList.keyValue
        Integer high = Integer.MIN_VALUE;
        for (Map<Integer, List<Coord>> map : movablePieces.values()) {      // Hämtar högsta rankvärde
            for (Integer rank : map.keySet()) {
                if (rank > high) {
                    high = rank;
                }
            }
        }
        final Integer fHigh = high;
        Map<Integer, List<Coord>> filteredList = new HashMap<>();

//       filteredList = movablePieces.entrySet().stream().flatMap((id -> id.getValue().entrySet().stream().filter(rank -> rank.getValue())));  // ? ?!?! ?!? ?! ? ?!? HJÄLP


        for (int i = 0; i < movablePieces.size(); i++) {
            for (int j = 0; j < movablePieces.size(); j++)
                filteredList.put(, )
            movablePieces.get(i).remove(j);
        }

        //Skicka tillbaka map<id,List<Coord> ...
        return movablePieces;
    }

    public boolean chooseMove(Player player, Player opponent) {
        Map<Integer, List<Coord>> movables = new HashMap<>(canMove(player.getPieces()));
        int randomIDpick, randomCoordPick;

        if (movables.size() == 0)
            return false;
        else {
            do {
                randomIDpick = ThreadLocalRandom.current().nextInt(0, 16);
            } while (!(movables.containsKey(randomIDpick)));

            List<Coord> coordList = movables.get(randomIDpick);
            if (coordList.size() < 1) {
                randomCoordPick = ThreadLocalRandom.current().nextInt(0, coordList.size());
            } else {
                randomCoordPick = 0;
            }
            return move(player, opponent, randomIDpick, coordList.get(randomCoordPick));
        }
    }
}
