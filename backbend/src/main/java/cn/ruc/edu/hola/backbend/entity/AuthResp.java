package cn.ruc.edu.hola.backbend.entity;

import cn.ruc.edu.hola.backbend.util.RandomStringGenerator;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public record AuthResp(String uuid, boolean ok) {
    public static void main(String[] args) {
        AuthResp authResp = new AuthResp(null, false);
        System.out.println(new Gson().toJson(authResp));

        List<String> resp = new ArrayList<>();
        resp.add(RandomStringGenerator.generateRandomUUID());

        System.out.println(new Gson().toJson(resp));
    }
}
