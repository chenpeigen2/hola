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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Indexer {
    private Indexer() {}

    /**
     * remapping struct config
     **/
    public static Map<String, Set<String>> remappingStruct(String filePath) {
        URL url = Main.class.getClassLoader().getResource(filePath);
        if (url == null) return null;
        try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject obj = element.getAsJsonObject();
            Map<String, Set<String>> ret = new HashMap<>();
            for (var entry : obj.entrySet()) {
                Set<String> set = new HashSet<>();
                getJsonKey(set, entry.getValue(), "");
                ret.put(entry.getKey(), set);
            }

            return ret;
        } catch (IOException | URISyntaxException ignored) {

        }
        return null;
    }

    /**
     * remapping the backbend config
     **/
    public static Map<String, String> remappingUrl(String filePath) {
        URL url = Main.class.getClassLoader().getResource(filePath);
        if (url == null) return null;
        try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject obj = element.getAsJsonObject();
            Map<String, String> ret = new HashMap<>();
            for (var entry : obj.entrySet()) {
                ret.put(entry.getKey(), entry.getValue().getAsString());
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
