package cn.edu.ruc.hola.web;

import cn.edu.ruc.hola.benchmark.Bench;
import cn.edu.ruc.hola.benchmark.Inspect;
import cn.edu.ruc.hola.entity.Response;
import cn.edu.ruc.hola.utils.anno.Core;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

@UseSpringBoot
public final class CouchVerticle extends AbstractVerticle {

    private final Gson json = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());
        router.get("/query").handler(this::query);
        router.get("/basic").handler(this::basicQuery);
        router.post("/postQuery").handler(this::postQuery);
        server.requestHandler(router).listen(8080);
    }

    /**
     * sample parameters
     * 0.95 100 80
     * <p>
     * condition:
     * {
     * "selector": {
     * "data.countryid": {"$gt": -1}
     * },
     * "execution_stats": true
     * }
     * <p>
     * {"selector": {"data.countryid": {"$gt": -1}},"execution_stats": true}
     * <p>
     * the sample url:
     * http://localhost:8080/basic?th=0.95&M=100&H=80&query={%22selector%22:%20{%22data.countryid%22:%20{%22$gt%22:%20-1}},%22execution_stats%22:%20true}
     */
    private void basicQuery(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();

        float threshHold = Float.parseFloat(request.getParam("th"));
        int M = Integer.parseInt(request.getParam("M"));
        int H = Integer.parseInt(request.getParam("H"));
        String query = request.getParam("query");

        Response response = Bench.INSTANCE.execute(threshHold, M, H, query);
        out(ctx, json.toJson(response));
    }

    @Core
    private void postQuery(RoutingContext ctx) {
        float threshHold = 0.95f;
        int M = 100;
        int H = 67;
        var query = "{\"selector\":{\"data.countryid\":{\"$gt\":-1}},\"execution_stats\":true}";

        Response response = Bench.INSTANCE.execute(threshHold, M, H, query);
        out(ctx, json.toJson(response));
    }

    /**
     * {
     * "th" : 0.95,
     * "M" : 100,
     * "H" : 80,
     * "query" : {"selector":{"data.countryid":{"$gt":-1}},"execution_stats":true}
     * }
     */
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
