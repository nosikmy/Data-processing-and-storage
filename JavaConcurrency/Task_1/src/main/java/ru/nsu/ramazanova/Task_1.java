package ru.nsu.ramazanova;

public class Task_1 extends Thread {
    public static void main(String[] args) {
        Runnable child = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("child " + i);
            }
        };
        Thread thread = new Thread(child);
        thread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("parent " + i);
        }
    }
}
