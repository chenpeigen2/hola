package cn.ruc.edu.hola.fakeTee;

import io.vertx.core.Vertx;

public class Entry {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Router.class.getName());
    }
}