package cn.ruc.edu.hola.index;

import cn.ruc.edu.hola.index.chaincode.ChainCodeBridge;
import cn.ruc.edu.hola.index.chaincode.Service;
import cn.ruc.edu.hola.index.config.Options;
import cn.ruc.edu.hola.index.json.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class ServiceRegisterVerticel extends AbstractVerticle {

    final ChainCodeBridge impl = Indexer2.impl;

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.post("/register").handler(ctx -> {
            Service ans;
            try {
                // do register_remapping
                String text = ctx.body().asString();
                RegisterInfo registerInfo = JsonUtils.INSTANCE.getJson().fromJson(text, RegisterInfo.class);
                ans = Indexer2.register(registerInfo); // do register
                System.out.println(JsonUtils.INSTANCE.getJson().toJson(ans));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            out(ctx, JsonUtils.INSTANCE.getJson().toJson(ans));
        });


        router.post("/unregister").handler(ctx -> {
            try {
                var json_arr = ctx.body().asJsonArray();
                for (int i = 0; i < json_arr.size(); i++) {
                    var serviceName = json_arr.getString(i);
                    Indexer2.unregister(serviceName);
                }
            } catch (Exception e) {
                out(ctx, "error");
            }
            out(ctx, "success");
        });


        router.get("/getAll").handler(ctx -> {
            String text = null;
            try {
                text = impl.getServices();
            } catch (Exception e) {
                out(ctx, text);
            }
            out(ctx, text);
        });


        router.get("/getSpec").handler(ctx -> {
            var serviceId = ctx.request().getParam("serviceId");
            if (serviceId == null) {
                out(ctx, "null");
            } else {
                var text = "execute exception";
                try {
                    text = JsonUtils.INSTANCE.getJson().toJson(impl.getSpecService("aaa"));
                } catch (Exception e) {
                    out(ctx, text);
                }
            }
        });

        var config = config().getJsonObject(ServiceRegisterVerticel.class.getSimpleName());
        server.requestHandler(router).listen(config.getInteger(Options.Port));
    }

    private void out(RoutingContext ctx, String msg) {
        if (msg == null) {
            ctx.fail(400);
            return;
        }
        System.err.println(msg);
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
