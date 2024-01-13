package cn.ruc.edu.hola.fakeTee;

import cn.ruc.edu.hola.fakeTee.bridge.IMainTeeECallBridge;
import cn.ruc.edu.hola.fakeTee.bridge.MainTeeBridge;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class Router extends AbstractVerticle {

    IMainTeeECallBridge mainTeeBridge = new MainTeeBridge();

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        io.vertx.ext.web.Router router = io.vertx.ext.web.Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        // tee init process
        ((MainTeeBridge) mainTeeBridge).init(); // we should init first


        router.post("/_complex").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                var postData = ctx.body().asString();
                var resp = mainTeeBridge.complexFilter(postData);
                out(ctx, resp);
            }
        });

        router.post("/_auth").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                var postData = ctx.body().asString();
                var resp = mainTeeBridge.authMainTee(postData);
                out(ctx, resp);
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
