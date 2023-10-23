package ru.nsu.ramazanova;

import java.util.concurrent.locks.Lock;

public class Philosopher implements Runnable {
    private final int id;
    private int spaghetti = 3;
    private final Lock rightFork;
    private final Lock leftFork;

    public Philosopher(int id, Lock rightFork, Lock leftFork) {
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
        System.out.println("The philosopher " + id + " has eaten spaghetti");
    }
}