package ru.nsu.ramazanova;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphoreChild = new Semaphore(0);
        Semaphore semaphoreParent = new Semaphore(1);
        Runnable child = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphoreChild.acquire();
                    System.out.println("child " + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                semaphoreParent.release();
            }
        };
        Thread thread = new Thread(child);
        thread.start();
        for (int i = 0; i < 10; i++) {
            semaphoreParent.acquire();
            System.out.println("parent " + i);
            semaphoreChild.release();

        }
    }
}