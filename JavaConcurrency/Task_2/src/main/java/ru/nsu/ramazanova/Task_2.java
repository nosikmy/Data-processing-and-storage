package ru.nsu.ramazanova;

public class Task_2{
    public static void main(String[] args) throws InterruptedException {
        Runnable child = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("child " + i);
            }
        };
        Thread thread = new Thread(child);
        thread.start();
        thread.join();
        for (int i = 0; i < 10; i++) {
            System.out.println("parent " + i);
        }
    }
}