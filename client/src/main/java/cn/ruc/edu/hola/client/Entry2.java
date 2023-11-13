package cn.ruc.edu.hola.client;

import cn.ruc.edu.hola.client.config.Options;
import cn.ruc.edu.hola.client.performance.PerformanceHandler;
import cn.ruc.edu.hola.client.web.Verticle1;
import cn.ruc.edu.hola.client.web.Verticle2;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.time.Duration;

import static cn.ruc.edu.hola.client.config.Options.Periodic_Millis;

/**
 * only for trigger the entry
 **/
public class Entry2 {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(Options.WorkerPoolSize));
        vertx.deployVerticle(Verticle2.class.getName());
    }

    private static void schedulePingPong(Vertx vertx) {
        vertx.setPeriodic(Duration.ofMillis(Periodic_Millis).toMillis(), PerformanceHandler.getInstance());
    }
}
