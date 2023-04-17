package cn.ruc.edu.hola.backbend.server;

import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class CouchVerticle2 extends CouchVerticle {

    @Override
    public void listen(HttpServer server, Router router) {
        server.requestHandler(router).listen(8082);
    }
}