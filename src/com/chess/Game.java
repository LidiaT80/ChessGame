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
            Thread.sleep(200);
            game1 = chooseMove(p1, p2);
            if (!game1)
                break;
            Thread.sleep(200);
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
        List<Coord> coordList, pathList;
        Map<Integer,List<Coord>> safeCoords=new HashMap<>();
        List<Integer> safeId=new ArrayList<>();

        int randomIDpick, randomCoordPick;
        Coord opponentCoord;
        boolean checkmate = false;

        if(!check){
            if (movables.size() == 0)
                return false;

            for (int id:highestRankMovables.keySet()){
                safeCoords.put(id,checkForOpponent(highestRankMovables.get(id), opponent));
                if(safeCoords.get(id).size()>0)
                    safeId.add(id);
            }
            if(safeId.size()==0){
                for (int id:highestRankMovables.keySet()){
                    safeCoords.put(id,checkForOpponent(unRankedMovables.get(id), opponent));
                    if(safeCoords.get(id).size()>0 && id!=kingId)
                        safeId.add(id);
                }
            }
            if(safeId.size()==0){
                for (int id:highestRankMovables.keySet()){
                    safeCoords.put(id,highestRankMovables.get(id));
                    if(safeCoords.get(id).size()>0 && id!=kingId)
                        safeId.add(id);
                }
            }
            randomIDpick = choosePiece(safeId);
            coordList=safeCoords.get(randomIDpick);
        }else {
            randomIDpick=kingId;
            coordList=checkForOpponent(unRankedMovables.get(kingId), opponent);

            if(coordList.size()==0){
                opponentCoord = findOpponent(player, opponent);

                for (int key : movables.keySet()) {
                    checkmate = true;
                    for (List<Coord> list: movables.get(key).values()) {
                        for (Coord c:list) {
                            if (c.x == opponentCoord.x && c.y == opponentCoord.y) {
                                checkmate = false;
                                randomIDpick=key;
                                coordList.add(opponentCoord);
                                break;
                            }
                        }
                        if (!checkmate)
                            break;
                    }
                    if (!checkmate)
                        break;
                }
                if(checkmate){
                   pathList=findPath(opponentCoord, player);
                    for (Coord c:pathList) {
                        for (int key:unRankedMovables.keySet()) {
                            if(key!=kingId){
                                for (Coord coord:unRankedMovables.get(key)) {
                                    if(c.x==coord.x && c.y==coord.y){
                                        checkmate=false;
                                        randomIDpick=key;
                                        coordList.add(coord);
                                        break;
                                    }
                                }
                                if(!checkmate)
                                    break;
                            }
                        }
                        if(!checkmate)
                            break;
                    }
                }
                if(checkmate){
                    System.out.println("Checkmate!!!");
                    return false;
                }
            }
            check=false;
        }

        randomCoordPick = ThreadLocalRandom.current().nextInt(0, coordList.size());
        if(opponent.getPieces().get(kingId).getPosition().x==coordList.get(randomCoordPick).x && opponent.getPieces().get(kingId).getPosition().y==coordList.get(randomCoordPick).y )
            return false;
        move(player, opponent, randomIDpick, coordList.get(randomCoordPick));

        checkKing(player, opponent);

        return true;
    }

    private List<Coord> checkForOpponent(List<Coord> coordList, Player opponent) {
        boolean safe;

        List<Coord> safeCoords = new ArrayList<>();
        for (Coord coord : coordList) {
            safe = true;
            for (int key:canMove(opponent.getPieces()).keySet()){
                if(opponent.getPieces().get(key) instanceof Pawn){
                    for (Coord c:((Pawn) opponent.getPieces().get(key)).killMove()){
                        if (c.x == coord.x && c.y == coord.y) {
                            safe = false;
                            break;
                        }
                    }
                }else {
                    for(int id:filterUnrankedMap(canMove(opponent.getPieces())).keySet()){
                        if(!(opponent.getPieces().get(key) instanceof Pawn)){
                            for (Coord c : filterUnrankedMap(canMove(opponent.getPieces())).get(id)) {
                                if (c.x == coord.x && c.y == coord.y) {
                                    safe = false;
                                    break;
                                }
                            }
                            if ((!safe))
                                break;
                        }

                    }
                }
                if ((!safe))
                    break;
            }

            if (safe)
                safeCoords.add(coord);
        }
        return safeCoords;
    }


    public int choosePiece(List<Integer> movables) {  //chooses a random piece from list by Id
        int randomIDpick;
        do {
            randomIDpick = ThreadLocalRandom.current().nextInt(0, 16);
        } while (!(movables.contains(randomIDpick)));
        return randomIDpick;
    }

    public void checkKing(Player player, Player opponent) { //checks if the king is threatened
        List<Coord> list;

        for (Piece p : player.getPieces().values()) {
            if (p instanceof Pawn)
                list=((Pawn) p).killMove();
            else
                list=filterUnrankedMap(canMove(player.getPieces())).get(p.getId());

                if(list!=null)
                    for (Coord c :list ) {
                        checkForCheck(c, player, opponent);
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

    public List<Coord> findPath(Coord opponentCoord, Player player){
        Coord kingCoord=player.getPieces().get(kingId).getPosition();
        List<Coord> pathCoords=new ArrayList<>();
        int range;

        if(opponentCoord.x==kingCoord.x){
            range=opponentCoord.y-kingCoord.y;
            for (int i = 1; i <Math.abs(range) ; i++) {
                if(range<0)
                    pathCoords.add(new Coord(kingCoord.x, kingCoord.y-i));
                else
                    pathCoords.add(new Coord(kingCoord.x, kingCoord.y+i));
            }
        }
        else if(opponentCoord.y==kingCoord.y){
            range=opponentCoord.x-kingCoord.x;
            for (int i = 1; i <Math.abs(range) ; i++) {
                if(range<0)
                    pathCoords.add(new Coord(kingCoord.x-i, kingCoord.y));
                else
                    pathCoords.add(new Coord(kingCoord.x+i, kingCoord.y));
            }
        }
        else if (Math.abs(opponentCoord.x-kingCoord.x)==Math.abs(opponentCoord.y-kingCoord.y)){
            range=opponentCoord.x-kingCoord.x;
            for (int i = 1; i <Math.abs(range) ; i++) {
                if(range<0)
                    pathCoords.add(new Coord(kingCoord.x-i, kingCoord.y-i));
                else
                    pathCoords.add(new Coord(kingCoord.x+i, kingCoord.y+i));
            }
        }
        return pathCoords;
    }
}
