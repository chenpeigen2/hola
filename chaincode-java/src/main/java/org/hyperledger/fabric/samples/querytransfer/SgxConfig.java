package org.hyperledger.fabric.samples.querytransfer;

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
}
