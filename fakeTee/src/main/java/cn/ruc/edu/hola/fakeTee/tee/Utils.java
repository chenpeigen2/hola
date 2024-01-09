package cn.ruc.edu.hola.fakeTee.tee;

import cn.ruc.edu.hola.fakeTee.tee.entity.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    private static final Map<String, String> clientAuthMap = new HashMap<>();


    public static boolean clientAuth(String source) {
        return true;
    }

    public static String clientGetAesKey(String uid) {
        return "";
    }

    public static List<Request> provideQuery() {
        return null;
    }
}
