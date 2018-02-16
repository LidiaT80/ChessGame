package com.chess;

import com.chess.pieces.King;
import com.chess.pieces.Knight;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Game {
    private Player p1;
    private Player p2;
    private Board board;
    private boolean check=false;
    int kingId=11;

    Game(Board board) throws InterruptedException {
        p1 = new Player("white");
        p2 = new Player("black");
        this.board = board;
        board.initPlayers(p1.getPieces(), p2.getPieces());
        boolean game1 = true;
        boolean game2 = true;
        while (game1 && game2) {

            Thread.sleep(1500);
            game1=chooseMove(p1, p2);
            if(!game1)
                break;
            Thread.sleep(1500);
            game2=chooseMove(p2, p1);

        }


    }


    public void move(Player player, Player opponent, int id, Coord destination) {
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
                if (piece instanceof Pawn) {
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

                    if(!(targetPiece==null) && targetPiece.getColor().equals(piece.getColor()) && !(piece instanceof Knight))
                        break;


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



    public boolean chooseMove(Player player, Player opponent) {
        Map<Integer, List<Coord>> movables = new HashMap<>(canMove(player.getPieces()));

        List<Coord> coordList;
        int randomIDpick, randomCoordPick;
        Coord opponentCoord;
        boolean checkmate=false;

        if(check){
            randomIDpick=kingId;
            coordList=movables.get(randomIDpick);
            if(!(coordList==null)){
                coordList=checkKingMove(coordList, opponent);
                randomCoordPick = ThreadLocalRandom.current().nextInt(0, coordList.size() );

                move(player, opponent, randomIDpick, coordList.get(randomCoordPick));
                check=false;
                checkKing(player, opponent);
                return true;
            }
            if(coordList==null){
                opponentCoord=findOpponent(player, opponent);

                for (int key:movables.keySet()) {
                    checkmate=true;
                    for (Coord c:movables.get(key)) {
                        if(c.x==opponentCoord.x && c.y==opponentCoord.y){
                            checkmate=false;
                            move(player, opponent, key, opponentCoord);
                            break;
                        }
                    }
                    if(!checkmate)
                        break;
                }
                if(checkmate){
                    System.out.println("Checkmate!!!");
                    return false;
                }
            }
            check=false;
        }else {
            if(movables.size()==0)
                return false;

            randomIDpick=choosePiece(movables);
            coordList = movables.get(randomIDpick);
            if(randomIDpick==kingId){
                coordList=checkKingMove(coordList, opponent);
            }
            randomCoordPick = ThreadLocalRandom.current().nextInt(0, coordList.size() );

            move(player, opponent, randomIDpick, coordList.get(randomCoordPick));
            checkKing(player, opponent);
        }
        return true;
    }

    public int choosePiece(Map<Integer, List<Coord>> movables){  //chooses a random piece from list by Id
        int randomIDpick;
        do {
            randomIDpick = ThreadLocalRandom.current().nextInt(0, 16);
        } while (!(movables.containsKey(randomIDpick)));
        return randomIDpick;
    }

    public void checkKing(Player player, Player opponent){ //checks if the king is threatened
        List<Coord> list;
        for (Piece p:player.getPieces().values()) {
            if(p instanceof Pawn)
                list=((Pawn) p).killMove();
            else
                list=p.possibleMoves();

            for (Coord c:list) {
                Piece targetPiece = board.checkPosition(c, player, opponent);
                if(targetPiece instanceof King && !(targetPiece.getColor().equals(p.getColor()))){
                System.out.println("Check!");
                check=true;
                }
            }
        }
    }

    public List<Coord> checkKingMove(List<Coord> kingMoves, Player opponent){ //checks which fields are safe to move to
        boolean movable;
        List<Coord> kingCoords= new ArrayList<>();
        for (Coord coord:kingMoves) {
            movable=true;
            for (List<Coord> list:canMove(opponent.getPieces()).values()) {
                if (list.contains(coord))
                        movable=false;
            }
            if(movable)
                kingCoords.add(coord);
        }
        return kingCoords;
    }
    public Coord findOpponent(Player player, Player opponent){   // identifies the opponent piece that threatens the king

        Map<Integer, List<Coord>> opponentMovables = new HashMap<>(canMove(opponent.getPieces()));
        int opponentId=-1;
        for (int key:opponentMovables.keySet()){
            for (Coord c:opponentMovables.get(key)) {
                if(c.x==player.getPieces().get(kingId).getPosition().x && c.y==player.getPieces().get(kingId).getPosition().y)
                    opponentId=key;
            }
        }
        return opponent.getPieces().get(opponentId).getPosition();
    }
}
