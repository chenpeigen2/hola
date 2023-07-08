package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType
public class Query {

    @Property
    private final String queryId;

    @Property
    private final String date;

    @Property
    private final String query;

    @Property
    private final String result;

    @Property
    private final String owner;

    public String getQueryId() {
        return queryId;
    }

    public String getDate() {
        return date;
    }

    public String getQuery() {
        return query;
    }

    public String getResult() {
        return result;
    }

    public String getOwner() {
        return owner;
    }

    public Query(final String queryId, final String date, final String query, final String result, final String owner) {
        this.queryId = queryId;
        this.date = date;
        this.query = query;
        this.result = result;
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query1 = (Query) o;
        return Objects.equals(queryId, query1.queryId) && Objects.equals(date, query1.date) && Objects.equals(query, query1.query) && Objects.equals(result, query1.result) && Objects.equals(owner, query1.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryId, date, query, result, owner);
    }

    @Override
    public String toString() {
        return "Query{" +
                "queryId='" + queryId + '\'' +
                ", date='" + date + '\'' +
                ", query='" + query + '\'' +
                ", result='" + result + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
