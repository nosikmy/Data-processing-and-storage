package ru.nsu.ramazanova;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public final class Founder {
    private final CyclicBarrier cyclicBarrier;
    private final List<Runnable> workers;
    public Founder(final Company company) {
        this.cyclicBarrier = new CyclicBarrier(company.getDepartmentsCount(), company::showCollaborativeResult);
        this.workers = new ArrayList<>(company.getDepartmentsCount());
        for (int i = 0; i < company.getDepartmentsCount(); i++) {
            int finalI = i;
            this.workers.add(() -> {
                Department department = company.getFreeDepartment(finalI);
                department.performCalculations();
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    public void start() {
        for (final Runnable worker : workers) {
            new Thread(worker).start();
        }
    }
}