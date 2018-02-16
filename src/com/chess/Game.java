package com.chess;

import com.chess.pieces.Knight;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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

            Thread.sleep(500);
            game1 = chooseMove(p1, p2);
            if (!game1)
                break;
            Thread.sleep(500);
            game2 = chooseMove(p2, p1);

        }


    }


    private boolean move(Player player, Player opponent, int id, Coord destination) {
        return board.movePiece(player, opponent, id, destination);
    }


    private Map<Integer, List<Coord>> canMove(Map<Integer, Piece> pieces) {
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
                                rankedCoordList.computeIfAbsent(targetPiece.getRank(), k -> new ArrayList<>()).add(pawnCoord); //lägg till coord i lista, skapa ny om lista ej finns
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
            if (rankedCoordList.size() != 0) {
                movablePieces.put(piece.getId(), rankedCoordList); //Slutligen lägg till pjäsens alla coords
            }
        }

        int high = Integer.MIN_VALUE;
        for (Map<Integer, List<Coord>> map : movablePieces.values()) {      // Hämtar högsta rankvärde
            for (Integer rank : map.keySet()) {
                if (rank > high) {
                    high = rank;
                }
            }
        }
        final Integer fHigh = high;

        //Filter movablePieces to only holding highest rank moves!
        movablePieces = movablePieces
                .entrySet()
                .stream()
                .collect(
                        Collectors
                                .toMap(
                                        Map.Entry::getKey, e -> e.getValue()
                                                .entrySet()
                                                .stream()
                                                .filter(rank -> rank.getKey()
                                                        .equals(fHigh))
                                                .collect(Collectors.toMap(
                                                        Map.Entry::getKey, Map.Entry::getValue))));


        //Input the map with killingMoves to outputMap
        Map<Integer, List<Coord>> filteredMap = new HashMap<>();
        Object[] id = movablePieces.keySet().toArray();
        for (int i = 0; i < movablePieces.size(); i++) {
            if (movablePieces.get(id[i]).containsKey(fHigh)) {
                filteredMap.put((Integer) id[i], movablePieces.get(id[i]).get(fHigh));
            }
        }

        return filteredMap;
    }

    private boolean chooseMove(Player player, Player opponent) {
        Map<Integer, List<Coord>> movables = new HashMap<>(canMove(player.getPieces()));
        int randomIDpick, randomCoordPick;

        if (movables.size() == 0)
            return false;
        else {
            do {
                randomIDpick = ThreadLocalRandom.current().nextInt(0, 16);
            } while (!(movables.containsKey(randomIDpick)));

            List<Coord> coordList = movables.get(randomIDpick);
            randomCoordPick = ThreadLocalRandom.current().nextInt(0, coordList.size());

            return move(player, opponent, randomIDpick, coordList.get(randomCoordPick));
        }
    }
}
