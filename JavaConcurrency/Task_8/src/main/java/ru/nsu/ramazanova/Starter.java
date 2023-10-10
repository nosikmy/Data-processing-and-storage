package ru.nsu.ramazanova;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Starter {
    private final int iterations;
    private final ExecutorService executorService;
    private final List<Task> tasks = new ArrayList<>();
    private double result = 0;
    private List<Future<Double>> futures;

    public Starter(int iterations, int threadsCount) {
        this.iterations = iterations;
        this.executorService = Executors.newFixedThreadPool(threadsCount);
    }

    public void start() throws InterruptedException, ExecutionException {
        int step = 10000000;
        for (int i = 1; i <= iterations && i > 0; i += step * 4) {
            tasks.add(new Task(i, step * 4));
        }
        futures = executorService.invokeAll(tasks);
        executorService.shutdown();
    }

    public void stop() {
        Main.setFlag(true);
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        result = 4 * futures.stream().map(x -> {
            try {
                return x.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).reduce(0D, Double::sum);
    }

    public double getResult() {
        return result;
    }
}