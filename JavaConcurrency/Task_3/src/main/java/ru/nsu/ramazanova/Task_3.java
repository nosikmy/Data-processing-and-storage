package ru.nsu.ramazanova;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task_3 {
    public static void main(String[] args) {
        List<String> input;
        Executor executor = (task) -> {
            (new Thread(task)).start();
        };
        for (int i = 0; i < 4; i++) {
            input = List.of(i + "-1", i + "-2", i + "-3");
            executor.execute(new Printer(input));
        }
    }


}

