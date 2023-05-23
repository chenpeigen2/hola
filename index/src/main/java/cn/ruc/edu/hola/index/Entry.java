package cn.ruc.edu.hola.index;

import io.vertx.core.Vertx;

import java.util.Map;
import java.util.Set;

public class Entry {
    public static Map<String, Set<String>> struct = Indexer.remappingStruct("struct.json");
    public static Map<String, String> backbend = Indexer.remappingUrl("backbend.json");

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Verticle.class.getName());
    }
}