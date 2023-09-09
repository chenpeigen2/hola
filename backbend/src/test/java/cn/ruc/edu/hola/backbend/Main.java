package cn.ruc.edu.hola.backbend;

import cn.ruc.edu.hola.backbend.db.CouchDBUtils;

import java.io.IOException;

@Deprecated
@TestOnly
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        CouchDBUtils couchDBUtils = new CouchDBUtils("127.0.0.1", 5984, "admin", "password");
        System.out.println(couchDBUtils.createDb("d1").body().string());
        var text = """
                {
                    "servings": 4,
                    "subtitle": "Delicious with fresh bread",
                    "title": "Fish Stew"
                }
                """;
        System.out.println(couchDBUtils.addDoc("d1", text).body().string());
        System.out.println(couchDBUtils.deleteDb("d1").body().string());
    }
}