package cn.ruc.edu.hola.client.performance;


import io.vertx.core.Handler;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public final class PerformanceHandler implements Handler<Long> {

    private PerformanceHandler() {
    }

    int cnt = 0;

    private final Queue<String> queue = new ArrayDeque<>();

    private static final Handler<Long> handler = new PerformanceHandler();

    public static Handler<Long> getInstance() {
        return handler;
    }

    public boolean hasResult(String re) {
        return !strings.contains(re);
    }

    private Set<String> strings = new HashSet<>();

    public synchronized void offer(String obj) {
        strings.add(obj);
        cnt++;
        System.out.println(obj);
        System.out.println("+++++++++++++++++++");
        queue.offer(obj);
        System.out.println(cnt);

    }

    @Override

    public void handle(Long event) {
        System.out.println(cnt);

        int size = queue.size();

        for (int i = 0; i < size; i++) {
            String x = queue.poll();
            System.out.println(x);
            synchronized (x) {
                x.notifyAll();
            }
        }

        strings.clear();

        System.out.println("enddddddddddddding");

    }
}
