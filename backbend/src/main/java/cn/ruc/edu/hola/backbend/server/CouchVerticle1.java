package cn.ruc.edu.hola.backbend.server;

import cn.ruc.edu.hola.backbend.config.Options;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class CouchVerticle1 extends CouchVerticle {
    public String getTag() {
        return "bb2";
    }

    @Override
    public Future<HttpServer> listen(HttpServer server, Router router) {
        var config = config().getJsonObject(CouchVerticle1.class.getSimpleName());
        port = config.getInteger(Options.Port);
        return server.requestHandler(router).listen(port);
    }
}