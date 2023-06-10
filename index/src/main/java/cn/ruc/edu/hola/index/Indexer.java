package cn.ruc.edu.hola.index;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

public final class Indexer {
    private Indexer() {}

    /**
     * remapping struct config
     **/
    public static Map<String, List<StructInfo>> remappingStruct(String filePath) {
        URL url = Indexer.class.getClassLoader().getResource(filePath);
        if (url == null) return null;
        try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
            // top object
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject obj = element.getAsJsonObject();
            // the result
            Map<String, List<StructInfo>> ret = new HashMap<>();

            for (var entry : obj.entrySet()) {
                List<StructInfo> list = new ArrayList<>();

                entry.getValue().getAsJsonArray().iterator().forEachRemaining(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement element) {
                                var structObject = element.getAsJsonObject();

                                var dbs = structObject.getAsJsonArray("dbs");
                                Set<String> db_set = new HashSet<>();
                                dbs.forEach(new Consumer<JsonElement>() {
                                    @Override
                                    public void accept(JsonElement element) {
                                        db_set.add(element.getAsString());
                                    }
                                });

                                var struct = structObject.getAsJsonObject("struct");
                                Set<String> struct_set = new HashSet<>();
                                getJsonKey(struct_set, struct, "");
                                list.add(new StructInfo(db_set, struct_set));
                            }
                        }
                );

                ret.put(entry.getKey(), list);
            }

            return ret;
        } catch (IOException | URISyntaxException ignored) {

        }
        return null;
    }

    /**
     * remapping the backbend config
     **/
    public static Map<String, BackBendInfo> remappingUrl(String filePath) {
        URL url = Indexer.class.getClassLoader().getResource(filePath);
        if (url == null) return null;
        try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject obj = element.getAsJsonObject();
            Map<String, BackBendInfo> ret = new HashMap<>();
            for (var entry : obj.entrySet()) {
                var jsonObj = entry.getValue().getAsJsonObject();
                var bb_url = jsonObj.get("url").getAsString();
                var bb_find = jsonObj.get("find").getAsString();
                ret.put(entry.getKey(), new BackBendInfo(bb_url, bb_find));
            }
            return ret;
        } catch (IOException | URISyntaxException ignored) {

        }
        return null;
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
