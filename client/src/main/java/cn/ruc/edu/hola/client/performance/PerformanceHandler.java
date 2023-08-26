package cn.ruc.edu.hola.client.performance;


import io.vertx.core.Handler;

public final class PerformanceHandler implements Handler<Long> {

    private PerformanceHandler() {
    }

    public static Handler<Long> getInstance() {
        return new PerformanceHandler();
    }

    @Override
    public void handle(Long event) {
        System.out.println(Thread.currentThread().getName());
    }
}
