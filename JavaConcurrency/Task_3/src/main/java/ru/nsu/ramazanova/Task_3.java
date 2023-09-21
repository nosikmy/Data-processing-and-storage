package ru.nsu.ramazanova;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task_3 {
    public static void main(String[] args) {
        List<List<String>> strings = new ArrayList<>();
        strings.add(new ArrayList<>(List.of("1-1", "1-2", "1-3")));
        strings.add(new ArrayList<>(List.of("2-1", "2-2", "2-3", "2-4")));
        strings.add(new ArrayList<>(List.of("3-1", "3-2")));
        strings.add(new ArrayList<>(List.of("4-1", "4-2", "4-3", "4-4", "4-5")));
        Executor executor = (task) -> {
            (new Thread(task)).start();
        };
        for (int i = 0; i < 4; i++) {
            executor.execute(new Printer(strings.get(i)));
        }
    }


}

