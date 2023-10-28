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

            Table.forks.lock();
            try {
                System.out.println("The philosopher " + id + " wait forks");
                while (true) {
                    if (!rightFork.isLocked()){
                        rightFork.lock();
                        System.out.println("The philosopher " + id + " take a right fork");
                        if(!leftFork.isLocked()){
                            leftFork.lock();
                            System.out.println("The philosopher " + id + " take a left fork");
                            break;
                        }
                        else {
                            rightFork.unlock();
                            System.out.println("The philosopher " + id + " put down a right fork");
                        }
                    }
                    Table.condition.await();
                }


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Table.forks.unlock();

            try {
                System.out.println("The philosopher " + id + " is eating...");
                Thread.sleep((long) (Math.random() * 10000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            spaghetti -= 1;

            Table.forks.lock();
            rightFork.unlock();
            System.out.println("The philosopher " + id + " put down a right fork");
            leftFork.unlock();
            System.out.println("The philosopher " + id + " put down a left fork");
            Table.condition.signalAll();
            Table.forks.unlock();

        }
        System.out.println("The philosopher " + id + " has eaten spaghetti");
    }
}