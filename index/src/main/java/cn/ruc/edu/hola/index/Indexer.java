package cn.ruc.edu.hola.index;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Indexer {
    private Indexer() {}

    /** remapping struct **/
    public static Map<String, Set<String>> remappingStruct(String json) {
        JsonElement element = JsonParser.parseString(json);
        JsonObject obj = element.getAsJsonObject();
        Map<String, Set<String>> ret = new HashMap<>();
        for (var entry : obj.entrySet()) {
            Set<String> set = new HashSet<>();
            getJsonKey(set, entry.getValue(), "");
            ret.put(entry.getKey(), set);
        }

        return ret;
    }

    /** remapping the backbend config **/
    public static Map<String,String> remappingUrl(String json) {
        // TODO
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
