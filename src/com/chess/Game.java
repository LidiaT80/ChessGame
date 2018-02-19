package com.chess;

import com.chess.pieces.King;
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
    private boolean check = false;
    int kingId = 11;

    Game(Board board) throws InterruptedException {
        p1 = new Player("white");
        p2 = new Player("black");
        this.board = board;
        board.initPlayers(p1.getPieces(), p2.getPieces());
        boolean game1 = true;
        boolean game2 = true;
        while (game1 && game2) {
            Thread.sleep(1000);
            game1 = chooseMove(p1, p2);
            if (!game1)
                break;
            Thread.sleep(1000);
            game2 = chooseMove(p2, p1);
        }


    }


    public void move(Player player, Player opponent, int id, Coord destination) {
        board.movePiece(player, opponent, id, destination);
    }


    private Map<Integer, Map<Integer, List<Coord>>> canMove(Map<Integer, Piece> pieces) {
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
                        else if (!(targetPiece == null) && !(targetPiece.getColor().equals(piece.getColor()))) {  //Om motståndare finns på rutan
                            rankedCoordList.computeIfAbsent(targetPiece.getRank(), k -> new ArrayList<>()).add(coord); //lägg till coord i lista, skapa ny om lista ej finns
                            break;
                        }
                        else if (targetPiece == null)                                         //om rutan är tom
                            rankedCoordList.computeIfAbsent(0, k -> new ArrayList<>()).add(coord); //lägg till coord i lista, skapa ny om lista ej finns

                    }
                }
            }
            if (rankedCoordList.size() != 0) {
                movablePieces.put(piece.getId(), rankedCoordList); //Slutligen lägg till pjäsens alla coords
            }
        }


        return movablePieces;
    }

    private Map<Integer, List<Coord>> filterRankedMap(Map<Integer, Map<Integer, List<Coord>>> movablesMap) {
        int high = Integer.MIN_VALUE;
        for (Map<Integer, List<Coord>> map : movablesMap.values()) {      // Hämtar högsta rankvärde
            for (Integer rank : map.keySet()) {
                if (rank > high) {
                    high = rank;
                }
            }
        }
        final Integer fHigh = high;

        //Filter movablePieces to only holding highest rank moves!
        movablesMap = movablesMap
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
        Object[] id = movablesMap.keySet().toArray();
        for (int i = 0; i < movablesMap.size(); i++) {
            if (movablesMap.get(id[i]).containsKey(fHigh)) {
                filteredMap.put((Integer) id[i], movablesMap.get(id[i]).get(fHigh));
            }
        }


        return filteredMap;
    }

    private Map<Integer, List<Coord>> filterUnrankedMap(Map<Integer, Map<Integer, List<Coord>>> inputMap) {
        Map<Integer, List<Coord>> filteredMap = new HashMap<>();
        Object[] id = inputMap.keySet().toArray();
        for (int i = 0; i < id.length; i++) {
            Object[] rank = inputMap.get(id[i]).keySet().toArray();
            List<Coord> coords = new ArrayList<>();
            for (int j = 0; j < rank.length ; j++) {
                coords.addAll(inputMap.get(id[i]).get(rank[j]));
            }
            filteredMap.put((Integer)id[i],coords);
        }
        //TODO make forLoops/Streams and extract all Coords to filteredMap
        return filteredMap;
    }

    private boolean chooseMove(Player player, Player opponent) {
        Map<Integer, Map<Integer, List<Coord>>> movables = new HashMap<>(canMove(player.getPieces()));

        Map<Integer, List<Coord>> highestRankMovables = new HashMap<>(filterRankedMap(movables));
        Map<Integer, List<Coord>> unRankedMovables = filterUnrankedMap(movables);
        List<Coord> coordList=new ArrayList<>();

        int randomIDpick, randomCoordPick;
        Coord opponentCoord;
        boolean checkmate = false;

        if (check) {
            randomIDpick = kingId;
            if(unRankedMovables.get(randomIDpick)!=null){
                for (Coord c:unRankedMovables.get(randomIDpick)){
                    coordList.add(c);
                }
            }

     /*       for (List<Coord> list:movables.get(randomIDpick).values()) {
                for(Coord c:list){
                    coordList.add(c);
                }
            }*/
            if (coordList.size()!=0) {
                coordList = checkKingMove(coordList, opponent);
                if(coordList.size()!=0) {
                    randomCoordPick = ThreadLocalRandom.current().nextInt(0, coordList.size());

                    move(player, opponent, randomIDpick, coordList.get(randomCoordPick));
                    check = false;
                    checkKing(opponent, player);
                    return true;
                }
            }
            if (coordList.size() == 0) {
                opponentCoord = findOpponent(player, opponent);

                for (int key : movables.keySet()) {
                    checkmate = true;
                    for (List<Coord> list: movables.get(key).values()) {
                        for (Coord c:list) {
                            if (c.x == opponentCoord.x && c.y == opponentCoord.y) {
                                checkmate = false;
                                move(player, opponent, key, opponentCoord);
                                break;
                            }
                        }

                    }
                    if (!checkmate)
                        break;
                }
                if (checkmate) {
                    System.out.println("Checkmate!!!");
                    return false;
                }
            }
            check = false;
        } else {
            if (movables.size() == 0)
                return false;


            do{
                randomIDpick = choosePiece(highestRankMovables);
                if (randomIDpick == kingId)
                    coordList = checkKingMove(unRankedMovables.get(randomIDpick), opponent);
                else
                    coordList = highestRankMovables.get(randomIDpick);
            }while (!(coordList.size()>0));
            randomCoordPick = ThreadLocalRandom.current().nextInt(0, coordList.size());

            move(player, opponent, randomIDpick, coordList.get(randomCoordPick));
            checkKing(player, opponent);
        }
        return true;
    }

    private List<Coord> checkKingMove(List<Coord> coordList, Player opponent) {
        boolean movable;

        List<Coord> kingCoords = new ArrayList<>();
        for (Coord coord : coordList) {
            movable = true;
            for (Map<Integer, List<Coord>> map : canMove(opponent.getPieces()).values()) {
                for (List<Coord> list:map.values()) {
                    for (Coord c : list) {
                        if (c.x == coord.x && c.y == coord.y)
                            movable = false;
                    }
                }
            }
            if (movable)
                kingCoords.add(coord);
        }
        return kingCoords;
    }


    public int choosePiece(Map<Integer, List<Coord>> movables) {  //chooses a random piece from list by Id
        int randomIDpick;
        do {
            randomIDpick = ThreadLocalRandom.current().nextInt(0, 16);
        } while (!(movables.containsKey(randomIDpick)));
        return randomIDpick;
    }

    public void checkKing(Player player, Player opponent) { //checks if the king is threatened

    /*    for (Piece p : player.getPieces().values()) {
            if (p instanceof Pawn) {
                for (Coord c : ((Pawn) p).killMove()) {
                    checkForCheck(c, player, opponent);
                }
            }else {
                for (Map<Integer, List<Coord>> map : canMove(player.getPieces()).values()) {
                    for (List<Coord> list:map.values()) {
                        for (Coord c : list) {
                           checkForCheck(c,player,opponent);
                        }
                    }
                }
            }
        }*/

            for (List<Coord> list:filterRankedMap(canMove(player.getPieces())).values()) {
                for (Coord c : list) {
                    checkForCheck(c,player,opponent);
                }
            }


    }

    public void checkForCheck(Coord c, Player player, Player opponent) {
        Piece targetPiece = board.checkPosition(c, player, opponent);
        if (targetPiece instanceof King && !(targetPiece.getColor().equals(player.getColor()))) {
            System.out.println("Check!");
            check = true;
        }
    }


    public Coord findOpponent(Player player, Player opponent) {   // identifies the opponent piece that threatens the king

        Map<Integer, Map<Integer, List<Coord>>> opponentMovables = new HashMap<>(canMove(opponent.getPieces()));
        int opponentId = -1;
        for (int key : opponentMovables.keySet()) {
            for (List<Coord> list: opponentMovables.get(key).values()) {
                for (Coord c : list) {
                    if (c.x == player.getPieces().get(kingId).getPosition().x && c.y == player.getPieces().get(kingId).getPosition().y)
                        opponentId = key;
                }
            }

        }
        return opponent.getPieces().get(opponentId).getPosition();

    }
}
