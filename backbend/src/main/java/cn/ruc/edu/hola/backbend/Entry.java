package cn.ruc.edu.hola.backbend;


import cn.ruc.edu.hola.backbend.server.CouchVerticle;


import cn.ruc.edu.hola.backbend.server.CouchVerticle1;
import cn.ruc.edu.hola.backbend.server.CouchVerticle2;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public final class Entry {

    public static void main(String[] args) {
        System.getProperties().setProperty("jdk.crypto.KeyAgreement.legacyKDF", "true");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(CouchVerticle.class.getName());
        vertx.deployVerticle(CouchVerticle1.class.getName());
        vertx.deployVerticle(CouchVerticle2.class.getName());
    }
}
