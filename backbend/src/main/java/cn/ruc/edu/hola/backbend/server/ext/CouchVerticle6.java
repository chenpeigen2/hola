package cn.ruc.edu.hola.backbend.server.ext;

import cn.ruc.edu.hola.backbend.server.CouchVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class CouchVerticle6 extends CouchVerticle {

    public String getTag() {
        return "bb7";
    }

    @Override
    public Future<HttpServer> listen(HttpServer server, Router router) {
//        var config = config().getJsonObject(CouchVerticle4.class.getSimpleName());
        port = 8086;
        return server.requestHandler(router).listen(port);
    }
}