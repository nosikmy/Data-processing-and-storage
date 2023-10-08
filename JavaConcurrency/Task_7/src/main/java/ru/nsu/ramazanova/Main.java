package ru.nsu.ramazanova;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Starter starter = new Starter(1000000000, Integer.parseInt(args[0]));
        System.out.println(starter.start());
    }
}