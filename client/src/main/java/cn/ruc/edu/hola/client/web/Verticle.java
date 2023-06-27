package cn.ruc.edu.hola.client.web;

import cn.ruc.edu.hola.client.Entry;
import cn.ruc.edu.hola.client.bridge.ChainCodeBridge;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Verticle extends AbstractVerticle {
    final static int PORT = 3001;

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.get("/trigger").handler(ctx -> {
            System.out.println("hello world");
            ChainCodeBridge bridge = new ChainCodeBridge();
//            Entry.triggerClient(); // trigger the query

            new Thread(new Runnable() {
                @Override
                public void run() {
                    bridge.triggerChaincode(); // trigger the chaincode

                }
            }).start();

            out(ctx, "trigger success");
        });


        server.requestHandler(router).listen(PORT);
    }

    private void out(RoutingContext ctx, String msg) {
        if (msg == null) {
            ctx.fail(400);
            return;
        }
        System.err.println(msg);
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
