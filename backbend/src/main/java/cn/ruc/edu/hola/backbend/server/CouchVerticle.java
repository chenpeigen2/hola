package cn.ruc.edu.hola.backbend.server;

import cn.ruc.edu.hola.backbend.config.Options;
import cn.ruc.edu.hola.backbend.entity.*;
import cn.ruc.edu.hola.backbend.json.JsonUtils;
import cn.ruc.edu.hola.backbend.register.Register;
import cn.ruc.edu.hola.backbend.register.Registers;
import com.google.gson.JsonSyntaxException;
import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
import java.util.List;

public class CouchVerticle extends AbstractVerticle {

    protected int port;

    final Proxy proxyMethods = new EncryptProxy();

    public String getHost() {
        return "127.0.0.1";
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return "/ping";
    }

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
    }

    private void doRegister(RegisterInfo registerInfo) {
        Registers.INSTANCE.doRegister(registerInfo);
    }

    public String getTag() {
        return "bb1";
    }

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.get("/_uuids").handler(this::uuids);
        router.get("/create_db").handler(this::createDb);
        router.get("/delete_db").handler(this::deleteDb);
        router.get("/add_doc").handler(this::addDoc);
        router.get("/delete_doc").handler(this::deleteDoc);
        router.post("/_find").handler(this::filterDocs);
        router.get("/_index").handler(this::createIndex);
        router.post("/_auth").handler(this::auth); // should change to post
        installPing(router); // 保活
        listen(server, router).onSuccess(event -> {
            RegisterInfo registerInfo = null;
            ServiceInfo serviceInfo = new ServiceInfo(getHost(), getPort(), getPath(), getTag(), getTag()); // service info
            if (getTag().equals("bb1")) {
                registerInfo = Register.register(serviceInfo, "./database/bb1_config.json");
            } else if (getTag().equals("bb2")) {
                registerInfo = Register.register(serviceInfo, "./database/bb2_config.json");
            } else if (getTag().equals("bb3")) {
                registerInfo = Register.register(serviceInfo, "./database/bb3_config.json");
            }
            System.out.println(registerInfo);
            if (registerInfo != null) {
                doRegister(registerInfo);
            }
        }).onFailure(new Handler<Throwable>() {
            @Override
            public void handle(Throwable event) {
                System.out.println("failing");
            }
        });
    }

    private void installPing(Router router) {
        router.get("/ping").handler(ctx -> {
            out(ctx, "pong");
        });
    }

    public Future<HttpServer> listen(HttpServer server, Router router) {
        var config = config().getJsonObject(CouchVerticle.class.getSimpleName());
        port = config.getInteger(Options.Port);
        return server.requestHandler(router).listen(port);
    }

    private void out(RoutingContext ctx, String msg) {
        if (msg == null) {
            ctx.fail(400);
            return;
        }
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }

    public void uuids(RoutingContext ctx) {
        String count = ctx.request().getParam("count");
        try {
            int cnt = Integer.parseInt(count);
            out(ctx, proxyMethods.uuids(cnt));
        } catch (NumberFormatException e) {
            ctx.fail(400);
        }
    }

    public void createDb(RoutingContext ctx) {
        String dataBaseName = ctx.request().getParam("name");
        try {
            out(ctx, proxyMethods.createDb(dataBaseName));
        } catch (NumberFormatException e) {
            ctx.fail(400);
        }
    }

    private void auth(RoutingContext ctx) {
        try {
            Auth auth = RequestEntitiesKt.toAuth(ctx.body().asString());
            String uuid = proxyMethods.acquireAESKey(auth);
            List<String> resp = new ArrayList<>();
            resp.add((uuid != null) ? uuid : "null");
            out(ctx, JsonUtils.INSTANCE.getJson().toJson(resp));
        } catch (JsonSyntaxException ignored) {
            ctx.fail(400);
        }
    }

    public String allDbs(RoutingContext ctx) {
        return null;
    }

    public String dbsInfo(RoutingContext ctx) {
        return null;
    }

    public String getDbInfo(RoutingContext ctx) {
        return null;
    }

    public void deleteDb(RoutingContext ctx) {
        String dataBaseName = ctx.request().getParam("name");
        try {
            out(ctx, proxyMethods.deleteDb(dataBaseName));
        } catch (NumberFormatException e) {
            ctx.fail(400);
        }
    }

    public void addDoc(RoutingContext ctx) {
        String dataBaseName = ctx.request().getParam("name");
        String data = ctx.request().getParam("data");
        try {
            out(ctx, proxyMethods.addDoc(dataBaseName, data));
        } catch (NumberFormatException e) {
            ctx.fail(400);
        }
    }

    public String addBulkDoc(RoutingContext ctx) {
        return null;
    }

    public void deleteDoc(RoutingContext ctx) {
        String dataBaseName = ctx.request().getParam("name");
        String data = ctx.request().getParam("docId");
        try {
            out(ctx, proxyMethods.deleteDoc(dataBaseName, data));
        } catch (NumberFormatException e) {
            ctx.fail(400);
        }
    }

    public String allDocs(RoutingContext ctx) {
        return null;
    }

    public String findSpecDoc(RoutingContext ctx) {
        return null;
    }

    public String findBulkDoc(RoutingContext ctx) {
        return null;
    }

    public void filterDocs(RoutingContext ctx) {
        Filter filterObject = RequestEntitiesKt.toFilter(ctx.body().asString());

        // ack the data
        String filter = filterObject.getFilter();
        String uuid = filterObject.getUuid();

        try {
            out(ctx, proxyMethods.filterDocsByBB(uuid, filter, getTag()));
        } catch (NumberFormatException e) {
            ctx.fail(400);
        }
    }

    public void createIndex(RoutingContext ctx) {
        String dataBaseName = ctx.request().getParam("db_name");
        String info = ctx.request().getParam("info");
        try {
            out(ctx, proxyMethods.createIndex(dataBaseName, info));
        } catch (NumberFormatException e) {
            ctx.fail(400);
        }
    }
}
