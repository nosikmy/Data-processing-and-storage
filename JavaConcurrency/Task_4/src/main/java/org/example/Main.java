package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Runnable child = () -> {
            while (!Thread.interrupted()){
                System.out.print("Текст");
            }
        };
        Thread thread = new Thread(child);
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
    }
}