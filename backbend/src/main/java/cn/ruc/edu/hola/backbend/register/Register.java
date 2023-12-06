package cn.ruc.edu.hola.backbend.register;

import cn.ruc.edu.hola.backbend.entity.RegisterInfo;
import cn.ruc.edu.hola.backbend.entity.ServiceInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Register {
    private Register() {}

    public static RegisterInfo register(ServiceInfo serviceInfo, String filePath) {
        RegisterInfo registerInfo = null;
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            registerInfo = new RegisterInfo(serviceInfo, jsonElement);
        } catch (IOException ignored) {

        }
        return registerInfo;
    }
}
