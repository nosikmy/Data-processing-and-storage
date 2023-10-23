package ru.nsu.ramazanova;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        AtomicBoolean run = new AtomicBoolean(false);
        Runnable child = () -> {
            for (int i = 0; i < 10; i++) {
                synchronized (lock){
                    while (!run.get()){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    run.set(false);
                    System.out.println("child " + i);
                    lock.notify();
                }

            }
        };
        Thread thread = new Thread(child);
        thread.start();
        for (int i = 0; i < 10; i++) {
            synchronized (lock){
                while (run.get()){
                    lock.wait();
                }
                run.set(true);
                System.out.println("parent " + i);
                lock.notify();
            }

        }
    }
}