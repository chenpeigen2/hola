package cn.ruc.edu.hola.backbend.server;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class CouchVerticle2 extends CouchVerticle {

    public String getTag() {
        return "bb3";
    }

    public int getPort() {
        return 8082;
    }

    @Override
    public Future<HttpServer> listen(HttpServer server, Router router) {
        return server.requestHandler(router).listen(8082);
    }
}