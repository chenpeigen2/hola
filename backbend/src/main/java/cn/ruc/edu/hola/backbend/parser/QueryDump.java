package cn.ruc.edu.hola.backbend.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryDump {

    public static List<String> dumpKeys(String query) {
        JsonObject object = JsonParser.parseString(query).getAsJsonObject();
        return dumpKeys(object.getAsJsonObject("selector"));
    }

    public static List<String> dumpKeys(JsonObject jsonObject) {
        List<String> ret = new ArrayList<>();
        for (Map.Entry<String, JsonElement> elementEntry : jsonObject.entrySet()) {
            if (elementEntry.getKey().equals("$or") || elementEntry.getKey().equals("$and")) {
                var arr = elementEntry.getValue().getAsJsonArray();
                for (JsonElement element : arr) {
                    ret.addAll(dumpKeys(element.getAsJsonObject()));
                }
                continue;
            }
            ret.add(elementEntry.getKey());
        }

        return ret;
    }
}
