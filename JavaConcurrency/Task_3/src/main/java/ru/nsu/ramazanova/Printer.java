package ru.nsu.ramazanova;

import java.util.List;

public class Printer implements Runnable {
    List <String> input;

    public Printer(List<String> input) {
        this.input = input;
    }

    @Override
    public void run() {
//        System.out.println(input);
        input.forEach(System.out::println);
    }
}
