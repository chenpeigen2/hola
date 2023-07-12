package cn.ruc.edu.hola.client.impl;

import cn.ruc.edu.hola.client.json.JsonUtils;
import com.google.gson.annotations.Expose;

public final class SgxConfig {

    @Expose
    private final String scheme;

    @Expose
    private final String host;

    @Expose
    private final int port;

    public String getScheme() {
        return scheme;
    }

    public SgxConfig(String scheme, String host, int port) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
    }


    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "SgxConfig{" +
                "scheme='" + scheme + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public static void main(String[] args) {
        SgxConfig sgxConfig = new SgxConfig("shceme", "host", 56);
        String text = JsonUtils.INSTANCE.getJson().toJson(sgxConfig);
        System.out.println(text);
        SgxConfig sgxConfig1 = JsonUtils.INSTANCE.getJson().fromJson(text, SgxConfig.class);
        System.out.println(sgxConfig1.host);
        System.out.println(sgxConfig1.port);
    }
}
