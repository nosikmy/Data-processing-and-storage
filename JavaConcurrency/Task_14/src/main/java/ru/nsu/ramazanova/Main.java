package ru.nsu.ramazanova;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore semaphoreA = new Semaphore(0);
        Semaphore semaphoreB = new Semaphore(0);
        Semaphore semaphoreC = new Semaphore(0);
        Semaphore semaphoreM = new Semaphore(0);
        Runnable A = () -> {
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("A is done");
                semaphoreA.release();
            }
        };
        Runnable B = () -> {
            while (true){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("B is done");
                semaphoreB.release();
            }
        };
        Runnable C = () -> {
            while (true){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("C is done");
                semaphoreC.release();
            }
        };
        Runnable module = () -> {
            while (true){
                try {
                    semaphoreA.acquire();
                    semaphoreB.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Module is done");
                semaphoreM.release();
            }
        };
        Runnable widget = () -> {
            while (true){
                try {
                    semaphoreC.acquire();
                    semaphoreM.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("WIDGET IS DONE");
            }
        };
        new Thread(A).start();
        new Thread(B).start();
        new Thread(C).start();
        new Thread(module).start();
        new Thread(widget).start();
    }
}