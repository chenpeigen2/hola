package cn.ruc.edu.hola.index;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;
import java.util.function.Consumer;

public class Main {


    static String text = """
            {
              "bb1": {
                "code": "i",
                "message": "s",
                "data": {
                  "id": "i",
                  "name": "s",
                  "bfirstletter": "s",
                  "logo": "s",
                  "country": "s",
                  "countryid": "i"
                }
              },
              "bb2": {
                "code": "i",
                "message": "s",
                "data": {
                  "id": "i",
                  "name": "s",
                  "bfirstletter": "s",
                  "logo": "s",
                  "country": "s",
                  "countryid": "i"
                }
              },
              "bb3": {
                "code": "i",
                "message": "s",
                "data": {
                  "id": "i",
                  "name": "s",
                  "bfirstletter": "s",
                  "logo": "s",
                  "country": "s",
                  "countryid": "i"
                }
              }
            }
            """;

    public static void main(String[] args) {
        var ans = Indexer.remappingStruct(text);
        System.out.println();
    }
}