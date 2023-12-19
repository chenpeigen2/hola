package cn.ruc.edu.hola.backbend.index;

import cn.ruc.edu.hola.backbend.entity.inner.$info;
import cn.ruc.edu.hola.backbend.entity.inner.$path;
import cn.ruc.edu.hola.backbend.entity.inner.StructInfo2;
import cn.ruc.edu.hola.backbend.server.AbstractProxy;
import cn.ruc.edu.hola.backbend.server.Proxy;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

public final class Indexer2 {

    public static void remappingStructFromFile(Map<$info, List<StructInfo2>> result, String filePath, Proxy proxy) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject obj = element.getAsJsonObject();

            {
                var couchHolder = obj.get("$source").getAsJsonObject();
                AbstractProxy.CouchEntities entities = new AbstractProxy.CouchEntities(couchHolder.get("host").getAsString(), couchHolder.get("port").getAsInt(),
                        couchHolder.get("name").getAsString(), couchHolder.get("password").getAsString());
                proxy.updateEntities(entities);
            }

            List<StructInfo2> structs = acquireStructInfo2(obj);
            result.put(acquireInfo(obj), structs);
        } catch (IOException ignored) {

        }
    }

    private static String acquireBBName(JsonObject obj) {
        JsonObject $info = obj.getAsJsonObject("$info");
        return $info.get("$name").getAsString();
    }

    private static String acquireBBUrl(JsonObject obj) {
        JsonObject $info = obj.getAsJsonObject("$info");
        return $info.get("$url").getAsString();
    }

    private static JsonObject acquireBBPath(JsonObject obj) {
        JsonObject $info = obj.getAsJsonObject("$info");
        return $info.get("$path").getAsJsonObject();
    }

    private static $info acquireInfo(JsonObject obj) {
        var url = acquireBBUrl(obj);
        var name = acquireBBName(obj);
        var $path = acquirePathInfo(acquireBBPath(obj));
        return new $info(name, url, $path);
    }

    private static List<$path> acquirePathInfo(JsonObject obj) {

        List<$path> pathList = new ArrayList<>();
        obj.entrySet().forEach(new Consumer<Map.Entry<String, JsonElement>>() {
            @Override
            public void accept(Map.Entry<String, JsonElement> elementEntry) {

                var path = elementEntry.getKey();
                var array = elementEntry.getValue().getAsJsonArray();

                Set<String> set = new HashSet<>();
                array.forEach(new Consumer<>() {
                    @Override
                    public void accept(JsonElement element) {
                        var keyWord = element.getAsString();
                        set.add(keyWord);
                    }
                });
                pathList.add(new $path(path, set));
            }
        });
        return pathList;
    }

    private static List<StructInfo2> acquireStructInfo2(JsonObject obj) {
        JsonArray $struct = obj.getAsJsonArray("$struct");

        List<StructInfo2> ret = new ArrayList<>();
        $struct.forEach(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement singleStruct) {
                JsonObject structObject = singleStruct.getAsJsonObject();
                String db = structObject.get("db").getAsString();
                Set<String> structs = new HashSet<>();
                getJsonKey(structs, structObject.get("struct"), "");
                StructInfo2 structInfo2 = new StructInfo2(db, structs);
                ret.add(structInfo2);
            }
        });

        return ret;
    }

    public static void getJsonKey(Set<String> ret, JsonElement element, String base) {
        JsonObject obj = element.getAsJsonObject();
        for (var entry : obj.entrySet()) {
            ret.add(base + entry.getKey());
            if (entry.getValue().isJsonObject()) {
                getJsonKey(ret, entry.getValue(), base + entry.getKey() + ".");
            }
        }
    }
}
