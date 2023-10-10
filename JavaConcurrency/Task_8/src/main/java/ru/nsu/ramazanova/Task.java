package ru.nsu.ramazanova;

import java.util.concurrent.Callable;

public class Task implements Callable<Double> {
    private final double i;
    private final int step;

    public Task(int i, int step) {
        this.i = i;
        this.step = step;
    }

    @Override
    public Double call() {
        double result = 0;
        for (double j = i; j < i + step && !Main.isFlag() && j > 0; j += 4) {
            result += 1 / j - 1 / (j + 2);
        }
        return result;
    }
}