package org.hyperledger.fabric.samples.querytransfer;

import com.google.gson.annotations.Expose;
import org.hyperledger.fabric.samples.querytransfer.json.JsonUtils;

public final class SgxConfig {

    @Expose
    private final String host;

    @Expose
    private final int port;

    public SgxConfig(String host, int port) {
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
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public static void main(String[] args) {
        SgxConfig sgxConfig = new SgxConfig("host", 56);
        String text = JsonUtils.INSTANCE.getJson().toJson(sgxConfig);
        System.out.println(text);
        SgxConfig sgxConfig1 = JsonUtils.INSTANCE.getJson().fromJson(text, SgxConfig.class);
        System.out.println(sgxConfig1.host);
        System.out.println(sgxConfig1.port);
    }
}
