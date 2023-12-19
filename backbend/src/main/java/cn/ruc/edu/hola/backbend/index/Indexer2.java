package cn.ruc.edu.hola.backbend.index;


import cn.ruc.edu.hola.backbend.entity.StructInfo2;
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

    public static Map<String, Set<String>> pathUrl = new HashMap<>();

    public static void remappingStructFromFile(Map<String, List<StructInfo2>> result, String filePath, Proxy proxy) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject obj = element.getAsJsonObject();

            {
                var couchHolder = obj.get("$source").getAsJsonObject();
                AbstractProxy.CouchEntities entities = new AbstractProxy.CouchEntities(couchHolder.get("host").getAsString(), couchHolder.get("port").getAsInt(),
                        couchHolder.get("name").getAsString(), couchHolder.get("password").getAsString());
                proxy.updateEntities(entities);
            }
            String bbName = acquireBBName(obj);
            List<StructInfo2> structs = acquireStructInfo2(obj);
            result.put(bbName, structs);
            pathUrl.put(bbName, acquirePathInfo(acquireBBPath(obj)));
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

    private static Set<String> acquirePathInfo(JsonObject obj) {
        Set<String> pathSet = new HashSet<>();
        obj.entrySet().forEach(elementEntry -> {
            var path = elementEntry.getKey();
            pathSet.add(path);
        });
        return pathSet;
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
