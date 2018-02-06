package com.chess;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        GameWindow window = new GameWindow();
        Game game = new Game(window.getBoard());

    }
}
