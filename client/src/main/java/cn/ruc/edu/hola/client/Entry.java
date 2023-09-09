package cn.ruc.edu.hola.client;

import cn.ruc.edu.hola.client.config.Options;
import cn.ruc.edu.hola.client.performance.PerformanceHandler;
import cn.ruc.edu.hola.client.web.PerformanceVerticle;
import cn.ruc.edu.hola.client.web.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.time.Duration;

import static cn.ruc.edu.hola.client.config.Options.Periodic_Millis;

/**
 * only for trigger the entry
 **/
public class Entry {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(Options.WorkerPoolSize));
        vertx.deployVerticle(Verticle.class.getName());
        vertx.deployVerticle(PerformanceVerticle.class.getName());
        schedulePingPong(vertx);
    }

    private static void schedulePingPong(Vertx vertx) {
        vertx.setPeriodic(Duration.ofMillis(Periodic_Millis).toMillis(), PerformanceHandler.getInstance());
    }
}
