package ru.nsu.ramazanova;

import java.util.ArrayList;
import java.util.List;

public class Printer implements Runnable {
    List <String> input;

    public Printer(List<String> input) {
        this.input = new ArrayList<>(input);
    }

    @Override
    public void run() {
        input.forEach(System.out::println);
    }
}
