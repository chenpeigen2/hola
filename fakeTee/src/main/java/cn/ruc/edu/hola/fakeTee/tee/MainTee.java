package cn.ruc.edu.hola.fakeTee.tee;

import cn.ruc.edu.hola.fakeTee.bridge.IMainTeeOCallBridge;
import cn.ruc.edu.hola.fakeTee.ocall.IMainTeeOCall;
import cn.ruc.edu.hola.fakeTee.tee.entity.Pair;
import cn.ruc.edu.hola.fakeTee.tee.entity.Request;

import java.sql.DataTruncation;

// define the method which include eCall and oCall
interface IMainTee extends IMainTeeOCall {
    // define your eCall
    String eCallComplexCurl(String data);

    String eCallAuth(String data);

    // define your oCall
    void inject(IMainTeeOCallBridge mainTeeBridge);
}

public final class MainTee implements IMainTee {
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
        print("hello");
        return "yyy";
    }

    // should be internal
    private IMainTeeOCallBridge mainTeeBridge;

    @Override
    public void inject(IMainTeeOCallBridge mainTeeBridge) {
        this.mainTeeBridge = mainTeeBridge;
    }

    @Override
    public void print(String... text) {
        mainTeeBridge.printString();
    }
}
