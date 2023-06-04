package cn.ruc.edu.hola.backbend.entity;

import com.google.gson.Gson;

public record AuthResp(String uuid, boolean ok) {
    public static void main(String[] args) {
        AuthResp authResp = new AuthResp(null, false);
        System.out.println(new Gson().toJson(authResp));
    }
}
