package cn.ruc.edu.hola.fakeTee.tee;

import cn.ruc.edu.hola.fakeTee.bridge.IMainTeeOCallBridge;
import cn.ruc.edu.hola.fakeTee.tee.entity.Pair;
import cn.ruc.edu.hola.fakeTee.tee.entity.Request;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.sql.DataTruncation;
import java.util.*;
import java.util.function.Consumer;

public interface IMainTeeECall {
    // define your eCall
    String eCallComplexCurl(String data);

    String eCallAuth(String data);

    // define your oCall
    void inject(IMainTeeOCallBridge mainTeeBridge);

    static IMainTeeECall getInstance() {
        return new MainTee();
    }
}

final class MainTee implements IMainTeeECall {

    private final Set<String> authSet = new HashSet<>();
    private final Map<String, String> authMap = new HashMap<>();
    private final Map<String, String> authUuidMap = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    {
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
    }

    private final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)).build();


    private boolean refreshIfNeeded() {
        return Timer.INSTANCE.shouldRefresh();
    }

    private String randomString(int len) {
        return RandomStringGenerator.generateRandomString(len);
    }

    private String generateAESKey() {
        return randomString(24);
    }

    private Pair authIfNeeded(String proto, String domain, int port) {
        String url = proto + "://" + domain + ":" + port + "/_auth";
        boolean refresh = refreshIfNeeded();
        if (authMap.containsKey(url) && !refresh) {
            return new Pair(authMap.get(url), authUuidMap.get(url));
        }

        String aesKey = generateAESKey();
        String encryptKey = RSA.rsaEncrypt(aesKey, Utils.RSA_PUBLIC_KEY);


        String body = "{\"key\":\"" +
                encryptKey +
                "\"}";

        RequestBody requestBody = RequestBody.create(body, MediaType.get("application/json"));

        var request = new okhttp3.Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .headers(Headers.of(headers))
                .tag("authIfNeeded#TEE")
                .build();

        Pair pair = null;
        try (var response = client.newCall(request).execute()) {
            var postData = response.body().string();
            // restore to the map
            String uuid = JsonParser.parseString(postData).getAsJsonArray().get(0).getAsString();
            authUuidMap.put(url, uuid);
            authMap.put(url, aesKey);
            pair = new Pair(aesKey, uuid);

        } catch (Exception e) {

        }

        return pair;
    }

    private String curlPost(String url, Request request) {
        HttpUrl httpUrl = HttpUrl.get(url);

        Pair authResult = authIfNeeded(httpUrl.isHttps() ? "https" : "http",
                httpUrl.host(), httpUrl.port());

        String aesKey = authResult.a;
        String uuid = authResult.b;

        String filter = request.filter;
        filter = AES.encryptAES(filter, aesKey);

        String body = "{\"uuid\":\"" + uuid + "\",\"filter\":\"" + filter + "\"}";
        RequestBody requestBody = RequestBody.create(body, MediaType.get("application/json"));
        var requestBB = new okhttp3.Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .headers(Headers.of(headers))
                .tag("curlPost#TEE")
                .build();
        String realResponse = null;
        try (var response = client.newCall(requestBB).execute()) {
            var postData = response.body().string();
            // decrypt
            postData = AES.decryptAES(postData, aesKey);
            realResponse = postData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return realResponse;
    }


    @Override
    public String eCallComplexCurl(String data) {
        List<Request> provideDecodeData = Utils.provideQuery(data);
        List<String> resp = new ArrayList<>();

        provideDecodeData.forEach(new Consumer<Request>() {
            @Override
            public void accept(Request request) {
                String returnData = curlPost(request.url, request);
                resp.add(returnData);
            }
        });

        String response = Aggregation.SimpleAggregation(resp);


        // encrypt
        String uid = "e51dce84-f2a4-43db-9b8f-311250ff0ba1";
        String aesKey = Utils.clientGetAesKey(uid);
        response = AES.encryptAES(response, aesKey);

        // return the data
        return response;
    }

    @Override
    public String eCallAuth(String data) {
        boolean authed = Utils.clientAuth(data);
        if (authed) return "true";
        return "false";
    }

    // should be internal
    private IMainTeeOCallBridge mainTeeOCallBridge;

    @Override
    public void inject(IMainTeeOCallBridge mainTeeOCallBridge) {
        this.mainTeeOCallBridge = mainTeeOCallBridge;
    }
}
