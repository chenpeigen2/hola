package cn.ruc.edu.hola.fakeTee.tee;

import cn.ruc.edu.hola.fakeTee.tee.entity.Request;
import com.google.gson.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Utils {
    private static final Map<String, String> clientAuthMap = new HashMap<>();

    private static final Gson gson = new Gson();

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

    static String bbUrl;
    static String bbFind;

    public static List<Request> provideQuery(String data) {
        // [{"uid":"e51dce84-f2a4-43db-9b8f-311250ff0ba1","filter":"I8Jr2dNWSTMXIBVmr3ep0Rn/cC6eEzG6h6PKetf1TRly2j+uv2wT5g73HnHloeyl6uFf7ycRODliMpENP8A6xtiRDYxxJV19h5tA19e4QlaecLhjv9rvYa4q4NexLYy5"}]
        // here we need offer fake query
        JsonArray jsonArray = JsonParser.parseString(data).getAsJsonArray();
        int len = jsonArray.size();
        List<Request> requests = new ArrayList<>();
        Map<String, Set<String>> urlRemoveMap = new HashMap<>();
        for (int i = 0; i < len; i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String uid = jsonObject.get("uid").getAsString();
            String filter = jsonObject.get("filter").getAsString();
            String aesKey = clientGetAesKey(uid);
            filter = AES.decryptAES(filter, aesKey);

            Set<String> notAppendUrl = new HashSet<>();

            // query split process start
            JsonObject object = JsonParser.parseString(filter).getAsJsonObject();
            JsonObject selectorObject = object.get("selector").getAsJsonObject();
            Set<String> keysSet = new HashSet<>();
            List<List<String>> nestedList = new ArrayList<>();
            Set<String> listAll = new HashSet<>();
            // detect the keyword $and
            if (selectorObject.get("$and") != null) {
                JsonArray $and = selectorObject.get("$and").getAsJsonArray();
                $and.forEach(element -> {
                    Set<String> keys = element.getAsJsonObject().keySet();
                    keysSet.addAll(keys);
                    List<String> vec = new ArrayList<>(keys);
                    listAll.addAll(vec);
                    nestedList.add(vec);
                });
            }

            Set<String> keys = selectorObject.keySet();
            if (nestedList.isEmpty()) {
                List<String> vec = new ArrayList<>(keys);
                nestedList.add(vec);
            } else {
                // [[health.heartRate], [medicalId]]

                keys.forEach(key -> {
                    if (!key.equals("$and")) {
                        for (List<String> list : nestedList) {
                            keysSet.add(key);
                            listAll.add(key);
                            list.add(key);
                        }
                    }
                });
            }
            // the split result
            List<List<String>> simpleSplitRes = new ArrayList<>(nestedList);

            // pre-match for sure
            List<String> urlList = Match.simpleMatch(gson.toJson(listAll));
            for (int k = 0; k < urlList.size(); ) {
                if (urlList.get(k).equals("$url")) {
                    k++; // move to $url value
                    bbUrl = urlList.get(k);
                    k++; // move to $find
                    if (urlList.get(k).equals("$find")) {
                        k++; // move to $find value
                        bbFind = urlList.get(k);

                        requests.add(new Request(bbUrl + bbFind, filter));
                        notAppendUrl.add(bbUrl + bbFind);

                    }
                    k++; // move to next
                }
            }


            for (List<String> vec : simpleSplitRes) {
                urlRemoveMap.clear();
                //                [health.heartRate, uid, date]
//                [medicalId, uid, date]
                System.out.println(vec);

                // compute the vec start
                Set<String> doHas = new HashSet<>();
                Set<String> doNotHas = new HashSet<>();
                // init the do has
                doHas.addAll(vec);
                // init the do not has
                for (String key : keysSet) {
                    if (!doHas.contains(key)) {
                        doNotHas.add(key);
                    }
                }
                // compute the vec end
                urlList = Match.simpleMatch(gson.toJson(vec));


                // compute the urlRemoveMap start
                for (int k = 0; k < urlList.size(); ) {
                    if (urlList.get(k).equals("$url")) {
                        k++; // move to $url value
                        bbUrl = urlList.get(k);
                        k++; // move to $find
                        if (urlList.get(k).equals("$find")) {
                            k++; // move to $find value
                            bbFind = urlList.get(k);

                            if (!urlRemoveMap.containsKey(bbUrl + bbFind)) {
                                Set<String> set = new HashSet<>(doNotHas);
                                urlRemoveMap.put(bbUrl + bbFind, set); // here
                            } else {
                                Set<String> set = urlRemoveMap.get(bbUrl + bbFind);
                                // if prev the key in that map, we need to remove the key
                                doHas.forEach(set::remove);
                                // if now the key do not in the map, we should  add in the map
                                set.addAll(doNotHas);
                            }

                        }
                        k++; // move to next
                    }
                }
                // compute the urlRemoveMap end


                // execute the query trim
                String finalFilter = filter;

                // {http://localhost:8084/_find/b7=[health.heartRate], http://localhost:8083/_find/b2=[health.heartRate], http://localhost:8085/_find/b2=[health.heartRate]}
                urlRemoveMap.forEach((url, set) -> {
                    String trimQuery = queryTrim(finalFilter, set);
                    if (!notAppendUrl.contains(url)) {
                        requests.add(new Request(url, trimQuery));
                    }
                });
                if (urlRemoveMap.isEmpty()) {
                    notAppendUrl.add(bbUrl + bbFind);
                    Set<Integer> set = new HashSet<>();
                    for (int k = 0; k < requests.size(); k++) {
                        if (notAppendUrl.contains(requests.get(k).url)) {
                            set.add(k);
                        }
                    }
                    for (Integer integer : set) {
                        requests.remove(integer);
                    }
                    requests.add(new Request(bbUrl + bbFind, finalFilter));
                }
            }

        }
        System.out.println("++++++++++++++++++++");
        for (Request request : requests) {
            System.out.println(request);
        }
        System.out.println("++++++++++++++++++++");

        return requests;
    }

    private static String queryTrim(String filter, Set<String> keysSet) {
        // {"selector":{"$and":[{"social_id":"羽毛球"},{"class_id":"17一班"}],"gender":0}}
        JsonObject jsonObject = JsonParser.parseString(filter).getAsJsonObject();
        JsonObject selectorObject = jsonObject.get("selector").getAsJsonObject();
        interJsonObject(selectorObject, keysSet);
        return gson.toJson(jsonObject);
    }

    private static void interJsonObject(JsonObject jsonObject, Set<String> keys) {
        for (String key : jsonObject.keySet()) {
            if (jsonObject.get(key).isJsonArray()) {
                interJsonArray(jsonObject.getAsJsonArray(key), keys);
                continue;
            }
            if (keys.contains(key)) {
                jsonObject.remove(key);
            }
        }
    }

    private static void interJsonArray(JsonArray jsonArray, Set<String> keys) {
        List<Integer> vec = new ArrayList<>();

        jsonArray.forEach(new Consumer<JsonElement>() {
            private int idx = 0;

            @Override
            public void accept(JsonElement element) {
                interJsonObject(element.getAsJsonObject(), keys);
                if (element.getAsJsonObject().isEmpty()) {
                    vec.add(idx);
                }
                idx++;
            }
        });

        for (Integer idx : vec) {
            jsonArray.remove(idx);
        }
    }


}
