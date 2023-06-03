package cn.ruc.edu.hola.backbend.server;

import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class CouchVerticle1 extends CouchVerticle {
    public String getTag() {
        return "bb2";
    }

    @Override
    public void listen(HttpServer server, Router router) {
        server.requestHandler(router).listen(8081);
    }
}