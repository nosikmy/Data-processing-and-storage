package ru.nsu.ramazanova;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Company company = new Company(10);
        Founder founder = new Founder(company);
        founder.start();
    }
}
