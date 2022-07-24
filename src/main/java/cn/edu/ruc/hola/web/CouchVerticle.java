package cn.edu.ruc.hola.web;

import cn.edu.ruc.hola.benchmark.Bench;
import cn.edu.ruc.hola.benchmark.Inspect;
import cn.edu.ruc.hola.entity.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;


public class CouchVerticle extends AbstractVerticle {

    private final Gson json = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());
        router.get("/query").handler(this::query);
        router.get("/basic").handler(this::basicQuery);
        server.requestHandler(router).listen(8080);
    }

    /**
     * sample parameters
     * 0.95 100 80
     *
     * condition:
     *           {
     *      "selector": {
     *       "data.countryid": {"$gt": -1}
     *                             },
     *        "execution_stats": true
     *       }
     *
     *        {"selector": {"data.countryid": {"$gt": -1}},"execution_stats": true}
     */

    private void basicQuery(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();

        float threshHold = Float.parseFloat(request.getParam("th"));
        int M = Integer.parseInt(request.getParam("M"));
        int H = Integer.parseInt(request.getParam("H"));
        String query = request.getParam("query");

        Response response = Bench.INSTANCE.basicExecute(threshHold, M, H, query);
        out(ctx, json.toJson(response));
    }

    private void query(RoutingContext ctx) {
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
