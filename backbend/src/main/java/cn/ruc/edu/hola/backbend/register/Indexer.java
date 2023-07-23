package cn.ruc.edu.hola.backbend.register;

import cn.ruc.edu.hola.backbend.entity.RegisterInfo;
import com.google.gson.*;
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

    public static void remappingUrl(String serviceId, String serviceName, String filePath) {
        URL url = Indexer.class.getClassLoader().getResource(filePath);
        if (url != null) {
            try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                var json_object = jsonElement.getAsJsonObject();

                RegisterInfo registerInfo = new RegisterInfo(serviceId, serviceName,
                        json_object.getAsJsonObject("$info").toString()
                        , json_object.getAsJsonArray("$struct").toString());
                System.out.println(jsonElement.toString());
                System.out.println(registerInfo);
            } catch (IOException | URISyntaxException ignored) {

            }
        }
    }

    public static void main(String[] args) {
        remappingUrl("", "", "bb1_config.json");
    }
}
