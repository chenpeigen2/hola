package cn.ruc.edu.hola.backbend.server;

import cn.ruc.edu.hola.backbend.entity.Auth;
import cn.ruc.edu.hola.backbend.entity.Filter;
import cn.ruc.edu.hola.backbend.json.JsonUtils;
import com.google.gson.JsonSyntaxException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import javax.naming.directory.SearchResult;

public class CouchVerticle extends AbstractVerticle {

    final static int PORT = 8080;

    final Proxy proxyMethods = new EncryptProxy();

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
        router.post("/_auth").handler(this::auth); // shoule change to post

        listen(server, router);
    }

    public void listen(HttpServer server, Router router) {
        server.requestHandler(router).listen(PORT);
    }

    private void out(RoutingContext ctx, String msg) {
        if (msg == null) {
            ctx.fail(400);
            return;
        }
        System.out.println(msg);
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
            System.err.println("auth !!!");
            Auth auth = JsonUtils.INSTANCE.toAuth(ctx.body().asString());
            boolean acquired = proxyMethods.acquireAESKey(auth.getKey());
            if (acquired) {
                out(ctx, """
                        {
                            "ok": true
                        }
                        """);

            } else {
                out(ctx, """
                        {
                            "ok": false
                        }
                        """);

            }
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
        Filter object = JsonUtils.INSTANCE.toFilter(ctx.body().asString());

//        String dataBaseName = object.getDbName();
        String filter = object.getFilter();

//        System.out.println(dataBaseName);
        System.out.println(filter);
        try {
            out(ctx, proxyMethods.filterDocs("fake1", filter));
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
