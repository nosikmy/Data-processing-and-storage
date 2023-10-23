package ru.nsu.ramazanova;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedList<String> list = new LinkedList<>();
        Runnable child = () -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (list) {
                    for (int i = 0; i < list.size() - 1; i++) {
                        for (int j = 0; j < list.size() - i - 1; j++) {
                            if (list.get(j + 1).compareTo(list.get(j)) < 0) {
                                String buf = list.get(j + 1);
                                list.set(j + 1, list.get(j));
                                list.set(j, buf);
                            }
                        }
                    }
                }
            }
        };
        Thread thread = new Thread(child);
        thread.start();
        while (true) {
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                synchronized (list) {
                    System.out.println("-----------");
                    list.forEach(System.out::println);
                    System.out.println("-----------");
                }
            } else {
                synchronized (list) {
                    list.add(input);
                }
            }
        }
    }
}


