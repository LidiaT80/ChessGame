package com.chess;

import javax.swing.*;

class GameWindow {
    private Board board;
    GameWindow() throws InterruptedException {
        board = new Board();
        JFrame frame=new JFrame();
        frame.setSize(600,600);
        board.createBoard();
        frame.add(board);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    Board getBoard(){
        return board;
    }

}