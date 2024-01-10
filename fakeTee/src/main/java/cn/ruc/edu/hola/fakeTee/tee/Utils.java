package cn.ruc.edu.hola.fakeTee.tee;

import cn.ruc.edu.hola.fakeTee.tee.entity.Request;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    private static final Map<String, String> clientAuthMap = new HashMap<>();

    public static String RSA_PRIVATE_KEY = "MIIBNgIBADANBgkqhkiG9w0BAQEFAASCASAwggEcAgEAAoGBALZrtub6dr1iEJ+JZhlTvH730yS0xb16XyyN36/QAS22H3k2H/p/tbNz3J3Cy44lK7w4oNadOwRgITjDInhmbDqcKlFiI7XPCC351EubsgiF1yOwjVKMsvKm1OgknVpvYTd2roKTWJ8yknfqWPxwsAUspGpbx51Jt/zuYXLFhlj/AgEAAoGAeZ0kmfxPKOwLFQZEEOJ9qfqMwyMuflGUyF6VH+AAySQU+3lqpv/Od6KTE9cyXsNyfXsV5GjSAurA0IIW+u7y0Jy+0IJuCTCt7vZwa4v+CA3Qy8sVBkbCve0KaFZIAYmdLKDfPOrKJRrLUVOoEM2oaaVfIvAhwLOqpBUmVwGa/zsCAQACAQACAQACAQACAQA=";
    public static String RSA_PUBLIC_KEY = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQC2a7bm+na9YhCfiWYZU7x+99MktMW9el8sjd+v0AEtth95Nh/6f7Wzc9ydwsuOJSu8OKDWnTsEYCE4wyJ4Zmw6nCpRYiO1zwgt+dRLm7IIhdcjsI1SjLLyptToJJ1ab2E3dq6Ck1ifMpJ36lj8cLAFLKRqW8edSbf87mFyxYZY/wIBAw==";


    public static boolean clientAuth(String source) {
        JsonObject jsonObject = JsonParser.parseString(source).getAsJsonObject();
        var uid = jsonObject.get("uid").getAsString();
        var key = jsonObject.get("key").getAsString();
        key = RSA.rsaDecrypt(key, RSA_PRIVATE_KEY);
        clientAuthMap.put(uid, key);
        return true;
    }

    public static String clientGetAesKey(String uid) {
        return clientAuthMap.getOrDefault(uid, null);
    }

    public static List<Request> provideQuery(String data) {
        // here we need offer fake query
        List<Request> requests = new ArrayList<>();
        requests.add(new Request("http://localhost:8080/_find/b2", "{\"selector\":{\"$or\":[{\"social_id\":\"羽毛球\"}],\"gender\":0}}"));
        requests.add(new Request("http://localhost:8081/_find/b7", "{\"selector\":{\"$or\":[{\"class_id\":\"17一班\"}],\"gender\":0}}"));
        return requests;
    }
}
