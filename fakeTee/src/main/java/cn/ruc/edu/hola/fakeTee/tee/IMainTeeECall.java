package cn.ruc.edu.hola.fakeTee.tee;

import cn.ruc.edu.hola.fakeTee.bridge.IMainTeeOCallBridge;
import cn.ruc.edu.hola.fakeTee.tee.entity.Pair;
import cn.ruc.edu.hola.fakeTee.tee.entity.Request;

import java.sql.DataTruncation;

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
        return "xxx";
    }

    @Override
    public String eCallAuth(String data) {
        mainTeeOCallBridge.printString("hello");
        return "yyy";
    }

    // should be internal
    private IMainTeeOCallBridge mainTeeOCallBridge;

    @Override
    public void inject(IMainTeeOCallBridge mainTeeOCallBridge) {
        this.mainTeeOCallBridge = mainTeeOCallBridge;
    }
}
