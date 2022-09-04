package cn.edu.ruc.hola.web;

import cn.edu.ruc.hola.utils.anno.Core;
import io.vertx.core.Vertx;

public final class Entry {

    @Core
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(CouchVerticle.class.getName());
        vertx.deployVerticle(Performance.class.getName());
    }
}
