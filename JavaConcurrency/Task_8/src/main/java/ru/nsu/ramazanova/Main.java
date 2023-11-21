package ru.nsu.ramazanova;

import java.util.concurrent.ExecutionException;

public class Main {
    private static Starter starter;
    private static boolean flag = false;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            starter.stop();
            System.out.println("Res = " + starter.getResult());
            System.out.println("Pi  = " + Math.PI);
            System.out.println("Dif = " + Math.abs(Math.PI - starter.getResult()));
        }));
        starter = new Starter(Integer.MAX_VALUE, Integer.parseInt(args[0]));
        starter.start();
    }

    public static boolean isFlag() {
        return flag;
    }

    public static void setFlag(boolean flag) {
        Main.flag = flag;
    }
}