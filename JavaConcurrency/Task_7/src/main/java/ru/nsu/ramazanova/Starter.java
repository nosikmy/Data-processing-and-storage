package ru.nsu.ramazanova;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Starter {
    private final int iterations;
    private final int threadsCount;

    public Starter(int iterations, int threadsCount) {
        this.iterations = iterations;
        this.threadsCount = threadsCount;
    }

    public Double start() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        List<Task> tasks = new ArrayList<>();
        int step = 1000;
        for (int i = 1; i <= iterations; i+= step *4) {
            tasks.add(new Task(i, step *4));
        }
        List<Future<Double>> futures = executorService.invokeAll(tasks);
        executorService.shutdown();
        return 4 * futures.stream().map(x -> {
            try {
                return x.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).reduce(0D, Double::sum);
    }
}
