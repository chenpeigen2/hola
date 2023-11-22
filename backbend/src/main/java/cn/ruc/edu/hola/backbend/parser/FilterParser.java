package cn.ruc.edu.hola.backbend.parser;

import cn.ruc.edu.hola.backbend.index.Indexer2;
import cn.ruc.edu.hola.backbend.index.StructInfo2;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

/**
 * parser the key we are using to indicate what we are looking for
 **/
public final class FilterParser {
    private FilterParser() {}

    public static Map<String, List<StructInfo2>> struct = Indexer2.remappingStructFromFile();

    /**
     * 可能会误伤其他的struct结构（暂时先不考虑）
     * <p>
     * 可能是伪需求 ！！！
     **/
    public static Set<String> getFilterDbs(String filter, String backbend_name) {
        JsonObject object = JsonParser.parseString(filter).getAsJsonObject();
        var selector_object = object.getAsJsonObject("selector");
        var keys = getKeys(selector_object);

        var structs = struct.get(backbend_name);
        if (structs == null) return null;

        Set<String> ret = new HashSet<>();

        keys.forEach(key -> {
            for (StructInfo2 structInfo2 : structs) {
                if (structInfo2.info().contains(key)) {
                    ret.add(structInfo2.db());
                }
            }
        });

        return ret;

    }

    public static List<String> getKeys(JsonObject jsonObject) {
        List<String> ret = new ArrayList<>();
        for (Map.Entry<String, JsonElement> elementEntry : jsonObject.entrySet()) {
            if (elementEntry.getKey().equals("$or") || elementEntry.getKey().equals("$and")) {
                var arr = elementEntry.getValue().getAsJsonArray();
                for (JsonElement element : arr) {
                    ret.addAll(getKeys(element.getAsJsonObject()));
                }
                continue;
            }
            ret.add(elementEntry.getKey());
        }

        return ret;
    }
}
