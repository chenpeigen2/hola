package cn.ruc.edu.hola.client.dataservice;

import cn.ruc.edu.hola.client.json.JsonUtils;
import com.google.gson.Gson;
import io.vertx.config.ConfigChange;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class DataServiceVerticle extends AbstractVerticle {
    final static int PORT = 3003;

    private JsonObject newConfig;

    private JsonObject oldConfig;

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.get("/list").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                out(ctx, newConfig.toString());
            }
        });

        router.post("/add/:productType").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                String productType = ctx.pathParam("productType");
                var body = ctx.body();
                var object = body.asJsonObject();
                JsonObject config = newConfig.copy();
                config.put(productType, object);
                DataServiceKt.writeFile(config.toString());
                out(ctx, productType + "\n" + object);
            }
        });

        router.delete("/add/:productType").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                String productType = ctx.pathParam("productType");
                JsonObject config = newConfig.copy();
                config.remove(productType);
                DataServiceKt.writeFile(config.toString());
                out(ctx, productType + "\n" + "removed");
            }
        });

        router.post("/purge").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                JsonObject config = new JsonObject();
                DataServiceKt.writeFile(config.toString());
                out(ctx, "purged");
            }
        });

        router.post("/query/:productType").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                String productType = ctx.pathParam("productType");
                var queryObject = newConfig.getJsonObject(productType);
                if (queryObject == null) {
                    out(ctx, "服务不存在！");
                } else {
                    out(ctx, queryObject.toString());
                }
            }
        });

        server.requestHandler(router).listen(PORT).onSuccess(new Handler<HttpServer>() {
            @Override
            public void handle(HttpServer event) {
                ConfigRetriever retriever = DataServiceKt.read(getVertx());
                retriever.listen(new Handler<ConfigChange>() {
                    @Override
                    public void handle(ConfigChange configChange) {
                        System.out.println("dump: " + newConfig);
                        newConfig = configChange.getNewConfiguration();
                        oldConfig = configChange.getPreviousConfiguration();
                        System.out.println("dump: newConfig = " + newConfig + ",oldConfig = " + oldConfig);
                    }
                });
            }
        });


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
