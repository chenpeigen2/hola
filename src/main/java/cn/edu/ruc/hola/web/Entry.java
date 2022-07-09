package cn.edu.ruc.hola.web;

import io.vertx.core.Vertx;

public class Entry {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(CouchVerticle.class.getName());
    }
}
