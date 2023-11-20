package cn.ruc.edu.hola.client.dataservice;

import io.vertx.config.ConfigChange;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.SharedData;
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

        router.post("/purge").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                JsonObject config = new JsonObject();
                DataService.writeFile(config.toString());
                out(ctx, "purged");
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
                DataService.writeFile(config.toString());
                out(ctx, productType + "\n" + object);
            }
        });

        router.delete("/add/:productType").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext ctx) {
                String productType = ctx.pathParam("productType");
                JsonObject config = newConfig.copy();
                config.remove(productType);
                DataService.writeFile(config.toString());
                out(ctx, productType + "\n" + "removed");
            }
        });


        server.requestHandler(router).listen(PORT).onSuccess(new Handler<HttpServer>() {
            @Override
            public void handle(HttpServer event) {
                ConfigRetriever retriever = DataService.read(getVertx());
                retriever.listen(new Handler<ConfigChange>() {
                    @Override
                    public void handle(ConfigChange configChange) {
                        System.out.println("dump: " + newConfig);
                        newConfig = configChange.getNewConfiguration();
                        oldConfig = configChange.getPreviousConfiguration();
                        SharedData sd = vertx.sharedData();
                        var map = sd.getLocalMap(DataService.DEFAULT_LOCAL_MAP_NAME);
                        map.put("newConfig", newConfig);
                        map.put("oldConfig", oldConfig);
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
