package cn.ruc.edu.hola.fakeTee;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Router extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        io.vertx.ext.web.Router router = io.vertx.ext.web.Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.post("/_complex").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                out(ctx, "_complex");
            }
        });

        router.post("/_auth").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                out(ctx, "_auth");
            }
        });

        router.get("/hello").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                out(ctx, "world");
            }
        });

        server.requestHandler(router).listen(8888);
    }

    private void out(RoutingContext ctx, String msg) {
        if (msg == null) {
            ctx.fail(400);
            return;
        }
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
