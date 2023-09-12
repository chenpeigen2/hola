package org.hyperledger.fabric.samples.querytransfer.directcall;

public class Body {
    private String uuid;

    private String filter;

    public Body(String uuid, String filter) {
        this.uuid = uuid;
        this.filter = filter;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
