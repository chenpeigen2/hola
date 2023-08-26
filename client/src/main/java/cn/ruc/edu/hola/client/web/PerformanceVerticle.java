package cn.ruc.edu.hola.client.web;

import cn.ruc.edu.hola.client.performance.Options;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class PerformanceVerticle extends Verticle {

    final static int PORT = 3002;

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        // define yr own query process
        router.post("/_find").handler(ctx -> {
            var postData = ctx.body().asString();
            postData += "\n";
            postData += Thread.currentThread().getName();
            out(ctx, postData);
        });

        router.post("/_find1").blockingHandler(ctx -> {
            var postData = ctx.body().asString();
            synchronized (Options.lock) {



                postData += "\n";
                postData += Thread.currentThread().getName();
                out(ctx, postData);
            }
        });

//        https://blog.csdn.net/qq_40276626/article/details/120381161
        router.post("/blocing")
                .blockingHandler(ctx -> {
                    HttpServerResponse response = ctx.response();
                    response.setChunked(true);
                    for (int i = 0; i < 10; i++) {
                        try {
                            Thread.sleep(500L);
                            response.write("test " + i + "\n");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ctx.end("over");
                }, true);

        server.requestHandler(router).listen(PORT);
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
