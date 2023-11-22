package cn.ruc.edu.hola.backbend.index;

import com.google.gson.JsonArray;
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

public final class Indexer2 {


    public static void main(String[] args) {
        var ans = remappingStructFromFile();
        System.out.println(ans);
    }


    public static Map<String, List<StructInfo2>> remappingStructFromFile() {
        Map<String, List<StructInfo2>> result = new HashMap<>();
        remappingStructFromFile(result, "bb1_config.json");
        remappingStructFromFile(result, "bb2_config.json");
        remappingStructFromFile(result, "bb3_config.json");
        return result;
    }


    private static void remappingStructFromFile(Map<String, List<StructInfo2>> result, String filePath) {
        URL url = Indexer2.class.getClassLoader().getResource(filePath);
        if (url == null) return;
        try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject obj = element.getAsJsonObject();
            String bbName = acquireBBName(obj);
            List<StructInfo2> structs = acquireStructInfo2(obj);
            result.put(bbName, structs);
        } catch (IOException | URISyntaxException ignored) {

        }
    }

    private static String acquireBBName(JsonObject obj) {
        JsonObject $info = obj.getAsJsonObject("$info");
        return $info.get("$name").getAsString();
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
