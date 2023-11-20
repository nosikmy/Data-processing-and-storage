package ru.nsu.ramazanova;

import java.nio.channels.AsynchronousFileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
    private final List<Philosopher> philosophers;
    public static Lock forks = new ReentrantLock();
    public static Condition condition = forks.newCondition();
    public Table(int philosophersCount) {
        List<ReentrantLock> lockList = new ArrayList<>(philosophersCount);
        for (int i = 0; i < philosophersCount; i++) {
            lockList.add(new ReentrantLock());
        }
        this.philosophers = new ArrayList<>();
        for (int i = 0; i < philosophersCount; i++) {
            philosophers.add(new Philosopher(i + 1, lockList.get(i), lockList.get((i+1)%philosophersCount)));
        }
    }

    public void run(){
        for (Philosopher p : philosophers) {
            new Thread(p).start();
        }
    }
}