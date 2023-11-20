package ru.nsu.ramazanova;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        AtomicBoolean run = new AtomicBoolean(true);
        Runnable child = () -> {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    try {
                        if(run.get()){
                            lock.notify();
                            run.set(false);
                        }
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("child " + i);
                    lock.notify();
                }

            }
        };
        Thread thread = new Thread(child);
        thread.start();
        for (int i = 0; i < 10; i++) {
            synchronized (lock) {
                lock.wait();
                System.out.println("parent " + i);
                lock.notify();
            }

        }
    }
}