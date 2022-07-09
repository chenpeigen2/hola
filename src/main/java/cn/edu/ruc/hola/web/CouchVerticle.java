package cn.edu.ruc.hola.web;

import cn.edu.ruc.hola.benchmark.Bench;
import cn.edu.ruc.hola.benchmark.Inspect;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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


public class CouchVerticle extends AbstractVerticle {

    private final Gson json = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

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

        router.get("/sync").handler(this::sync);
        server.requestHandler(router).listen(8080);
    }

    private void addUser(RoutingContext ctx) {
        String username = ctx.request().getParam("username");

        System.out.println(username);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "success");
        out(ctx, Json.encodePrettily(result));
    }

    private void sync(RoutingContext ctx) {
        String cnt = ctx.request().getParam("cnt");
        Inspect inspect;
        if (cnt == null) {
            inspect = Bench.INSTANCE.executeSync(1);
        } else {
            inspect = Bench.INSTANCE.executeSync(Integer.parseInt(cnt));
        }
        out(ctx, json.toJson(inspect));
    }

    private void out(RoutingContext ctx, String msg) {
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
