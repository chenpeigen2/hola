package cn.ruc.edu.hola.backbend;

import cn.ruc.edu.hola.backbend.okhttp.CouchDBUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        CouchDBUtils couchDBUtils = new CouchDBUtils("127.0.0.1", 5984, "admin", "password");
        System.out.println(couchDBUtils.createDb("d1").body().string());
        System.out.println(couchDBUtils.deleteDb("d1").body().string());
    }
}