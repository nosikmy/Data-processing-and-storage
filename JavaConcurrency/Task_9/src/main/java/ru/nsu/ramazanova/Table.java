package ru.nsu.ramazanova;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Table {

    private final List<Philosopher> philosophers;
    public Table(int philosophersCount, int spaghetti) {
        List<ReentrantLock> forks = new ArrayList<>(philosophersCount);
        for (int i = 0; i < philosophersCount; i++) {
            forks.add(new ReentrantLock());
        }
        this.philosophers = new ArrayList<>();
        for (int i = 0; i < philosophersCount; i++) {
            philosophers.add(new Philosopher(i + 1, forks.get(i), forks.get((i+1)%philosophersCount), spaghetti));
        }
    }

    public void run(){
        for (Philosopher p : philosophers) {
            new Thread(p).start();
        }
    }
}
