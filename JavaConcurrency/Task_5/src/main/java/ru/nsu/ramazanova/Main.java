package ru.nsu.ramazanova;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Runnable child = () -> {
            while (!Thread.interrupted()){
                System.out.print("Текст");
            }
            System.out.println("");

        };
        Thread thread = new Thread(child);
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();

        System.out.println("Interrupted");
    }
}