package cn.ruc.edu.hola.backbend;


import cn.ruc.edu.hola.backbend.server.CouchVerticle;
import io.vertx.core.Vertx;

public final class Entry {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(CouchVerticle.class.getName());
    }
}
