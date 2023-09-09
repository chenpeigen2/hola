package cn.ruc.edu.hola.index;

import io.vertx.core.Vertx;

import java.time.Duration;

public class Entry {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        schedulePingPong(vertx);
        vertx.deployVerticle(Verticle.class.getName());
        vertx.deployVerticle(ServiceRegisterVerticel.class.getName());
    }

    // schedule 1 minus delay to ping the backbend server
    private static void schedulePingPong(Vertx vertx) {
        vertx.setPeriodic(Duration.ofMinutes(1).toMillis(), event -> Ping.pong());
    }
}