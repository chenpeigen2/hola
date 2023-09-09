package cn.ruc.edu.hola.index;

import cn.ruc.edu.hola.index.json.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.*;

public class Verticle extends AbstractVerticle {
    final static int PORT = 3000;

    public static Map<String, List<StructInfo>> struct;
    public static Map<String, BackBendInfo> backbend;

    static {
        if (PropsConfig.supportIndexer2()) {
            struct = Indexer2.struct;
            backbend = Indexer2.backbend;
        } else {
            struct = Indexer.remappingStruct("struct.json");
            backbend = Indexer.remappingUrl("backbend.json");
        }
    }

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.post("/index").handler(ctx -> {
            var jsonArray = ctx.body().asJsonArray(); // we expect to return the arr
            Map<String, List<String>> ret = new HashMap<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                var keyWord = jsonArray.getString(i);

                struct.forEach((k, v) -> {

                    v.forEach(structInfo -> {
                        if (structInfo.info().contains(keyWord)) {
                            var list = ret.getOrDefault(keyWord, new ArrayList<>());
                            list.add(k);
                            ret.put(keyWord, list);
                        }
                    });

                });
            }
            out(ctx, JsonUtils.INSTANCE.getJson().toJson(ret));
        });

        router.post("/com").handler(ctx -> {
            var jsonArray = ctx.body().asJsonArray(); // we expect to return the arr
            List<BackBendInfo> ret = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                var keyWord = jsonArray.getString(i);
                ret.add(backbend.getOrDefault(keyWord, null));
            }
            out(ctx, JsonUtils.INSTANCE.getJson().toJson(ret));
        });

        router.post("/index_com").handler(ctx -> {
            var jsonArray = ctx.body().asJsonArray(); // we expect to return the arr
            Map<String, List<BackBendInfo>> ret = new HashMap<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                var keyWord = jsonArray.getString(i);
                struct.forEach((k, v) -> {
                    v.forEach(structInfo -> {
                        if (structInfo.info().contains(keyWord)) {
                            var list = ret.getOrDefault(keyWord, new ArrayList<>());
                            list.add(backbend.getOrDefault(k, null));
                            ret.put(keyWord, list);
                        }
                    });
                });
            }
            out(ctx, JsonUtils.INSTANCE.getJson().toJson(ret));
        });

        router.post("/aggregate").handler(ctx -> {
            var jsonArray = ctx.body().asJsonArray(); // we expect to return the arr
            List<List<String>> ret = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                var keyWord = jsonArray.getString(i);
                List<String> list = new ArrayList<>();
                struct.forEach((k, v) -> {
                    v.forEach(structInfo -> {
                        if (structInfo.info().contains(keyWord)) {
                            list.add(k);
                        }
                    });
                });
                ret.add(list);
            }
            out(ctx, JsonUtils.INSTANCE.getJson().toJson(retainElementList(ret)));
        });

        router.post("/aggregate_com").handler(ctx -> {
            var jsonArray = ctx.body().asJsonArray(); // we expect to return the arr

            List<String> keyWords = new ArrayList<>();

            List<List<String>> ret = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                var keyWord = jsonArray.getString(i);
                keyWords.add(keyWord);  // keyWords add
                List<String> list = new ArrayList<>();
                struct.forEach((k, v) -> {
                    v.forEach(structInfo -> {
                        if (structInfo.info().contains(keyWord)) {
                            list.add(k);
                        }
                    });
                });
                ret.add(list);
            }

            List<BackBendInfo> ans = new ArrayList<>();
            retainElementList(ret).forEach(re -> ans.add(backbend.getOrDefault(re, null)));
            // aggregate treat as list
            List<String> aggregate = new ArrayList<>();
            ans.forEach(backBendInfo -> {
                aggregate.add("$url");
                aggregate.add(backBendInfo.url());
                aggregate.add("$find");
                aggregate.add(backBendInfo.getPath(keyWords));
            });

            out(ctx, JsonUtils.INSTANCE.getJson().toJson(aggregate));
        });

        server.requestHandler(router).listen(PORT);

    }

    public static List<String> retainElementList(List<List<String>> elementLists) {

        Optional<List<String>> result = elementLists.parallelStream()
                .filter(elementList -> elementList != null && !elementList.isEmpty())
                .reduce((a, b) -> {
                    a.retainAll(b);
                    return a;
                });
        return result.orElse(new ArrayList<>());
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
