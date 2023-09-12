package org.hyperledger.fabric.samples.querytransfer.directcall;

public class Body {
    private String body;

    private String filter;

    public Body(String body, String filter) {
        this.body = body;
        this.filter = filter;
    }
}
