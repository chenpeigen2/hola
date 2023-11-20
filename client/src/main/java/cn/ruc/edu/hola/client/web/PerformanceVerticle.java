package cn.ruc.edu.hola.client.web;

import cn.ruc.edu.hola.client.dataservice.DataService;
import cn.ruc.edu.hola.client.entity.ChaincodeRequest;
import cn.ruc.edu.hola.client.parser.QueryParser;
import cn.ruc.edu.hola.client.performance.Callback;
import cn.ruc.edu.hola.client.performance.PerformanceHandler;
import cn.ruc.edu.hola.client.utils.RandomStringGenerator;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
import java.util.List;

public class PerformanceVerticle extends Verticle {

    final static int PORT = 3002;


    private void realExecute(RoutingContext ctx, String postData) {
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
    }

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.post("/multi_find").blockingHandler(ctx -> {
            var postData = ctx.body().asString();
            realExecute(ctx, postData);
        });

        router.post("/query/:productType").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                String productType = ctx.pathParam("productType");
                SharedData sharedData = vertx.sharedData();
                var map = sharedData.getLocalMap(DataService.DEFAULT_LOCAL_MAP_NAME);
                JsonObject newConfig = (JsonObject) map.get("newConfig");
                JsonObject oldConfig = (JsonObject) map.get("oldConfig");
                var queryObject = newConfig.getJsonObject(productType);
                if (queryObject == null) {
                    out(ctx, "服务不存在！");
                } else {
                    var postData = queryObject.toString();
                    realExecute(ctx, postData);
                }
            }
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
