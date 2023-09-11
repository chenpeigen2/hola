package org.hyperledger.fabric.samples.querytransfer.directcall;

public final class Request {
    public final String url;
    public final String filter;

    public Request(String url, String filter) {
        this.url = url;
        this.filter = filter;
    }
}
