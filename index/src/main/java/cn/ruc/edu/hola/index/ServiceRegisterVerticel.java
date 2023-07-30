package cn.ruc.edu.hola.index;

import cn.ruc.edu.hola.index.chaincode.ChainCodeBridge;
import cn.ruc.edu.hola.index.chaincode.Service;
import cn.ruc.edu.hola.index.json.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.UUID;

public class ServiceRegisterVerticel extends AbstractVerticle {
    final static int PORT = 2999;

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
                Indexer2.register(registerInfo); // do register

                ans = impl.register(registerInfo.serviceId(), registerInfo.serviceName());
                System.out.println(JsonUtils.INSTANCE.getJson().toJson(ans));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            out(ctx, JsonUtils.INSTANCE.getJson().toJson(ans));
        });

        // TODO
        router.get("/unregister").handler(ctx -> {
            try {
                impl.unregister("aaa");
            } catch (Exception e) {
                out(ctx, "error");
            }
            out(ctx, "success");
        });

        // TODO
        router.get("/getAll").handler(ctx -> {
            String text = "a";
            try {
                text = impl.getServices();
            } catch (Exception e) {
                out(ctx, text);
            }
            out(ctx, text);
        });

        // TODO
        router.get("/getSpec").handler(ctx -> {
            String text = "a";
            try {
                text = JsonUtils.INSTANCE.getJson().toJson(impl.getSpecService("aaa"));
            } catch (Exception e) {
                System.out.println("77777");
                out(ctx, text);
            }
            out(ctx, text);
        });

        server.requestHandler(router).listen(PORT);
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
