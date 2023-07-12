package cn.ruc.edu.hola.client;


import java.util.Objects;


public final class Query {


    private final String queryID; // 查询的唯一标识


    private final String owner; // 查询的所属owner


    private final String date; // 查询的时间


    private final String query; // 查询的请求内容


    private final String resp;

    public String getQueryID() {
        return queryID;
    }

    public String getOwner() {
        return owner;
    }

    public String getDate() {
        return date;
    }


    public String getQuery() {
        return query;
    }

    public String getResp() {
        return resp;
    }

    public Query(String queryID, String owner, String date, String query, String resp) {
        this.queryID = queryID;
        this.owner = owner;
        this.date = date;
        this.query = query;
        this.resp = resp;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Query other = (Query) obj;

        return Objects.deepEquals(
                new String[]{getQueryID(), getOwner(), getDate(), getQuery()},
                new String[]{other.getQueryID(), other.getOwner(), other.getDate(), other.getQuery()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQueryID(), getOwner(), getDate(), getQuery());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [queryID=" + queryID + ", owner=" + owner + "]";
    }
}
