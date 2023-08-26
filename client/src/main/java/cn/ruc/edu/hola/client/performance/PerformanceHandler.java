package cn.ruc.edu.hola.client.performance;


import io.vertx.core.Handler;

import java.util.ArrayDeque;
import java.util.Queue;

public final class PerformanceHandler implements Handler<Long> {

    private PerformanceHandler() {}

    private final Queue<Callback> queue = new ArrayDeque<>();

    private static final Handler<Long> handler = new PerformanceHandler();

    public static Handler<Long> getInstance() {
        return handler;
    }

    public synchronized void offer(Callback obj) {
        queue.offer(obj);
    }

    @Override

    public void handle(Long event) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            Callback x = queue.poll();
            x.onFinish("" + Thread.currentThread().getName() + System.currentTimeMillis());
        }
    }
}
