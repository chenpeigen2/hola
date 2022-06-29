package cn.edu.ruc.hola.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.HashMap;
import java.util.Map;


public class MyFirstVerticle extends AbstractVerticle {
    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.get("/user").handler(this::addUser);

        router.get("/").handler(request -> {
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/plain");
            // Write to the response and end it
            response.end("Hello World!");
        });

        server.requestHandler(router);

        server.listen(8080);
    }

    private void addUser(RoutingContext ctx) {
        String username = ctx.request().getParam("username");

        System.out.println(username);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "success");
        out(ctx, Json.encodePrettily(result));
    }

    private void out(RoutingContext ctx, String msg) {
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
