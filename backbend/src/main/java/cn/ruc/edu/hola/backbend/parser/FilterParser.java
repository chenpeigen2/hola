package cn.ruc.edu.hola.backbend.parser;

import cn.ruc.edu.hola.backbend.index.Indexer;
import cn.ruc.edu.hola.backbend.index.StructInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;
import java.util.function.Consumer;

/**
 * parser the key we are using to indicate what we are looking for
 **/
public final class FilterParser {
    private FilterParser() {
    }

    public static Map<String, List<StructInfo>> struct = Indexer.remappingStruct("struct.json");

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

        keys.forEach(new Consumer<String>() {
            @Override
            public void accept(String key) {
                for (StructInfo structInfo : structs) {
                    if (structInfo.getInfo().contains(key)) {
                        ret.addAll(structInfo.getDbs());
                    }
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

    /**
     * only for test
     **/
    public static void main(String[] args) {
        String query = """
                {
                  "selector": {
                    "$or": [
                      {
                        "data.countryid": {
                          "$gt": -1
                        }
                      },
                      {
                        "message": "success"
                      }
                    ],
                    "data.id": 0
                  },
                  "execution_stats": true
                }""";

        var ans = getFilterDbs(query, "bb1");

        System.out.println(ans);

    }
}
