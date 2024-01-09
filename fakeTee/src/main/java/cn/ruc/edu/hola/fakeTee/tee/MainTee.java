package cn.ruc.edu.hola.fakeTee.tee;

import cn.ruc.edu.hola.fakeTee.bridge.MainTeeBridge;
import cn.ruc.edu.hola.fakeTee.tee.entity.Pair;
import cn.ruc.edu.hola.fakeTee.tee.entity.Request;

import java.sql.DataTruncation;

// define the method which include eCall and oCall
interface IMainTee {
    // define your eCall
    String eCallComplexCurl(String data);

    String eCallAuth(String data);

    // define your oCall
    void inject(MainTeeBridge mainTeeBridge);

    void oCallPrint(String... text);
}

public final class MainTee implements IMainTee {
    boolean refreshIfNeeded() {
        return true;
    }

    String randomString(int len) {
        return "";
    }

    String generateAESKey() {
        return randomString(24);
    }

    Pair authIfNeeded(DataTruncation proto, String domain, int port) {
        return null;
    }

    String curlPost(String url, Request request) {
        return null;
    }


    @Override
    public String eCallComplexCurl(String data) {
        return null;
    }

    @Override
    public String eCallAuth(String data) {
        oCallPrint("hello");
        return "";
    }

    // should be internal
    private MainTeeBridge mainTeeBridge;

    @Override
    public void inject(MainTeeBridge mainTeeBridge) {
        this.mainTeeBridge = mainTeeBridge;
    }

    @Override
    public void oCallPrint(String... text) {
        mainTeeBridge.printString();
    }
}
