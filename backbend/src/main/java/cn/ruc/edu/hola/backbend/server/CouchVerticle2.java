package cn.ruc.edu.hola.backbend.server;

import cn.ruc.edu.hola.backbend.config.Options;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class CouchVerticle2 extends CouchVerticle {

    public String getTag() {
        return "bb3";
    }

    @Override
    public Future<HttpServer> listen(HttpServer server, Router router) {
        var config = config().getJsonObject(CouchVerticle2.class.getSimpleName());
        port = config.getInteger(Options.Port);
        return server.requestHandler(router).listen(port);
    }
}