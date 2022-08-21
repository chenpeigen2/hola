package cn.edu.ruc.hola.web;

import cn.edu.ruc.hola.benchmark.Bench;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public final class Performance extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.post("/performance").handler(this::performance);
        server.requestHandler(router).listen(8081);
    }

    private void performance(RoutingContext ctx) {
        RequestBody requestBody = ctx.body();

        JsonObject jsonObject = requestBody.asJsonObject();
        float threshHold = jsonObject.getFloat("th");
        int M = jsonObject.getInteger("M");
        int H = jsonObject.getInteger("H");
        var query = jsonObject.getJsonObject("query");

        Bench.INSTANCE.performance(threshHold, M, H, query.toString());

        out(ctx, "hello world !");
    }

    private void out(RoutingContext ctx, String msg) {
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
