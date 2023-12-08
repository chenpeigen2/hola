package cn.ruc.edu.hola.client.web;

import cn.ruc.edu.hola.client.ApiService;
import cn.ruc.edu.hola.client.config.Options;
import cn.ruc.edu.hola.client.entity.ChaincodeRequest;
import cn.ruc.edu.hola.client.parser.QueryParser;
import cn.ruc.edu.hola.client.performance.SGXEntity;
import cn.ruc.edu.hola.client.utils.RandomStringGenerator;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
import java.util.List;

public class Verticle extends AbstractVerticle {

    private final ApiService impl = ApiService.getInstance();

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        // define yr own query process
        router.post("/_find").handler(ctx -> {
            impl.update(SGXEntity.scheme, SGXEntity.host, SGXEntity.port); // select associate
            var postData = ctx.body().asString();
            var jsonData = QueryParser.basicParser(postData); // expected jsonData
            List<ChaincodeRequest> chaincodeRequests = new ArrayList<>();
            chaincodeRequests.add(new ChaincodeRequest(RandomStringGenerator.UID, jsonData));
            var text = impl.complex(chaincodeRequests).getDecryptResponse();
            out(ctx, text);
        });
        var config = config().getJsonObject(io.vertx.core.Verticle.class.getSimpleName());
        server.requestHandler(router).listen(config.getInteger(Options.Port));
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
