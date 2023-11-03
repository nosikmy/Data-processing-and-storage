package ru.nsu.ramazanova;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher implements Runnable {
    private final int id;
    private int spaghetti = 3;
    private final ReentrantLock rightFork;
    private final ReentrantLock leftFork;

    public Philosopher(int id, ReentrantLock rightFork, ReentrantLock leftFork) {
        this.id = id;
        this.rightFork = rightFork;
        this.leftFork = leftFork;
    }

    @Override
    public void run() {
        while (spaghetti != 0) {
            System.out.println("The philosopher " + id + " is thinking...");
            try {
                Thread.sleep((long) (Math.random() * 10000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            rightFork.lock();
            System.out.println("The philosopher " + id + " take a right fork");
            if(leftFork.isLocked()){
                rightFork.unlock();
                System.out.println("The philosopher " + id + " put down a right fork");
            }
            else {
                leftFork.lock();
                System.out.println("The philosopher " + id + " take a left fork");
                try {
                    System.out.println("The philosopher " + id + " is eating...");
                    Thread.sleep((long) (Math.random() * 10000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                spaghetti -= 1;
                rightFork.unlock();
                System.out.println("The philosopher " + id + " put down a right fork");
                leftFork.unlock();
                System.out.println("The philosopher " + id + " put down a left fork");
            }
        }
        System.out.println("The philosopher " + id + " has eaten spaghetti");
    }
}