package cn.ruc.edu.hola.index;

import cn.ruc.edu.hola.index.chaincode.ChainCodeBridge;
import cn.ruc.edu.hola.index.chaincode.Service;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;
import java.util.function.Consumer;


/**
 * indexer2 using the backbend register info
 */
public final class Indexer2 {
    private Indexer2() {}

    public static Map<String, List<StructInfo2>> struct = new HashMap<>();
    public static Map<String, BackBendInfo> backbend = new HashMap<>();
    public static Map<BackBendConfigInfo, String> registerPath = new HashMap<>();
    public static Map<String, String> services = new HashMap<>();

    public final static ChainCodeBridge impl = new ChainCodeBridge();

    public static Service register(RegisterInfo registerInfo) {
        registerPath.put(registerInfo.backBendConfigInfo(), registerInfo.serviceName()); // ping-pong usage
        services.put(registerInfo.serviceName(), registerInfo.serviceId());
        doRemappingStruct(registerInfo.serviceName(), registerInfo.struct());
        doRemappingInfo(registerInfo.serviceName(), registerInfo.info());
        Service service;
        try {
            service = impl.register(registerInfo.serviceId(), registerInfo.toString()); // restore the register info data
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return service;
    }

    public static void unregister(String serviceName) {
        struct.remove(serviceName);
        backbend.remove(serviceName);
        try {
            impl.unregister(services.get(serviceName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void doRemappingStruct(String serviceName, String text) {
        JsonElement jsonElement = JsonParser.parseString(text);
        List<StructInfo2> list = new ArrayList<>();
        jsonElement.getAsJsonArray().iterator().forEachRemaining(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement singleStruct) {
                JsonObject structObject = singleStruct.getAsJsonObject();
                String db = structObject.get("db").getAsString();
                Set<String> structs = new HashSet<>();
                getJsonKey(structs, structObject.get("struct"), "");
                StructInfo2 structInfo2 = new StructInfo2(db, structs);
                list.add(structInfo2);
            }
        });

        struct.put(serviceName, list);
    }

    private static void doRemappingInfo(String serviceName, String text) {
        JsonElement element = JsonParser.parseString(text);
        JsonObject jsonObj = element.getAsJsonObject();
        var bb_url = jsonObj.get("$url").getAsString();

        var path_obj = jsonObj.get("$path").getAsJsonObject();

        BackBendInfo backBendInfo = new BackBendInfo(bb_url);

        {
            // execute the path_obj
            for (Map.Entry<String, JsonElement> entry_path : path_obj.entrySet()) {
                String path = entry_path.getKey();
                JsonElement keywords = entry_path.getValue();
                // Do something with key and value

                BackBendInfo.PathMapping pathMapping = new BackBendInfo.PathMapping(path);
                keywords.getAsJsonArray().forEach(jsonElement -> pathMapping.put(jsonElement.getAsString()));

                backBendInfo.putPath(pathMapping);
            }
        }

        backbend.put(serviceName, backBendInfo);
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
