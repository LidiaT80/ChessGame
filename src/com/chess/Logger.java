package com.chess;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static Logger instance;
    private List<String> log=new ArrayList<>();

    private Logger(){}

    public static Logger getInstance(){
        if (instance==null)
            instance=new Logger();
        return instance;
    }

    public void logMove(String move){
        log.add(move);
    }
    public void printMove(String move){
        System.out.println(move);
    }
}
