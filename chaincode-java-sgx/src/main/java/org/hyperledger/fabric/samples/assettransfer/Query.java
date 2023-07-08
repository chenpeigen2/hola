package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType
public final class Query {

    @Property
    private final String queryId;

    @Property
    private final String date;

    @Property
    private final String req;

    @Property
    private final String resp;

    @Property
    private final String owner;

    public String getQueryId() {
        return queryId;
    }

    public String getDate() {
        return date;
    }

    public String getReq() {
        return req;
    }

    public String getResp() {
        return resp;
    }

    public String getOwner() {
        return owner;
    }

    public Query(final String queryId, final String date, final String req, final String resp, final String owner) {
        this.queryId = queryId;
        this.date = date;
        this.req = req;
        this.resp = resp;
        this.owner = owner;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return Objects.equals(queryId, query.queryId) && Objects.equals(date, query.date) && Objects.equals(req, query.req) && Objects.equals(resp, query.resp) && Objects.equals(owner, query.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryId, date, req, resp, owner);
    }

    @Override
    public String toString() {
        return "Query{" +
                "queryId='" + queryId + '\'' +
                ", date='" + date + '\'' +
                ", req='" + req + '\'' +
                ", resp='" + resp + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
