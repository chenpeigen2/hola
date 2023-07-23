package cn.ruc.edu.hola.backbend.register;

import cn.ruc.edu.hola.backbend.entity.RegisterInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Indexer {
    private Indexer() {}

    public static RegisterInfo remappingUrl(String serviceId, String serviceName, String filePath) {
        RegisterInfo registerInfo = null;
        URL url = Indexer.class.getClassLoader().getResource(filePath);
        if (url != null) {
            try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                var json_object = jsonElement.getAsJsonObject();
                registerInfo = new RegisterInfo(serviceId, serviceName,
                        json_object.getAsJsonObject("$info").toString()
                        , json_object.getAsJsonArray("$struct").toString());
            } catch (IOException | URISyntaxException ignored) {

            }
        }
        return registerInfo;
    }

    public static void main(String[] args) {
        var abc = remappingUrl("", "", "bb1_config.json");
        System.out.println(abc);
    }
}
