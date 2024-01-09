package cn.ruc.edu.hola.fakeTee.tee;

import cn.ruc.edu.hola.fakeTee.tee.entity.Pair;
import cn.ruc.edu.hola.fakeTee.tee.entity.Request;

import java.sql.DataTruncation;

interface IMainTee {
    String eCallComplexCurl(String data);

    int eCallAuth(String data);
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
    public int eCallAuth(String data) {
        return 0;
    }
}
