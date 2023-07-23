package cn.ruc.edu.hola.backbend.register;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.vertx.core.json.Json;

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
     * remapping the backbend config
     **/
    public static void remappingUrl(String filePath) {
//        URL url = Indexer.class.getClassLoader().getResource(filePath);
//        if (url == null) return null;
//        try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
//            JsonElement element = JsonParser.parseReader(reader);
//            JsonObject obj = element.getAsJsonObject();
////            new Gson().fromJson()
//        } catch (IOException | URISyntaxException ignored) {
//
//        }
//        return null;
    }
}
