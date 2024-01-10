package cn.ruc.edu.hola.fakeTee.tee;

import cn.ruc.edu.hola.fakeTee.bridge.IMainTeeOCallBridge;
import cn.ruc.edu.hola.fakeTee.tee.entity.Pair;
import cn.ruc.edu.hola.fakeTee.tee.entity.Request;

import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.List;
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
    private boolean refreshIfNeeded() {
        return true;
    }

    private String randomString(int len) {
        return "";
    }

    private String generateAESKey() {
        return randomString(24);
    }

    private Pair authIfNeeded(DataTruncation proto, String domain, int port) {
        return null;
    }

    private String curlPost(String url, Request request) {
        return null;
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
