package cn.ruc.edu.hola.client.web;

import cn.ruc.edu.hola.client.entity.ChaincodeRequest;
import cn.ruc.edu.hola.client.parser.QueryParser;
import cn.ruc.edu.hola.client.performance.Callback;
import cn.ruc.edu.hola.client.performance.PerformanceHandler;
import cn.ruc.edu.hola.client.utils.RandomStringGenerator;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
import java.util.List;

public class PerformanceVerticle extends Verticle {

    final static int PORT = 3002;

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.post("/multi_find").blockingHandler(ctx -> {

            var postData = ctx.body().asString();
            var jsonData = QueryParser.basicParser(postData); // expected jsonData
            List<ChaincodeRequest> chaincodeRequests = new ArrayList<>();
            chaincodeRequests.add(new ChaincodeRequest(RandomStringGenerator.UID, jsonData));
            PerformanceHandler handler = (PerformanceHandler) PerformanceHandler.getInstance();
            Callback callback = new Callback(chaincodeRequests) {
                @Override
                public void onFinish(String text) {
                    out(ctx, text);
                }
            };

            handler.offer(callback);
        });

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
