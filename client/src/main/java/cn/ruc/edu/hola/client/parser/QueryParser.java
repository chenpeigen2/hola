package cn.ruc.edu.hola.client.parser;

import com.google.gson.JsonParser;

public class QueryParser {

    public static boolean checkJsonString(final String query) {
        var element = JsonParser.parseString(query);
        return element.isJsonObject() || element.isJsonArray();
    }

    public static String basicParser(final String query) {
        if (!checkJsonString(query)) throw new IllegalArgumentException("not an json string !");
        return query;
    }
}
