package cn.ruc.edu.hola.client.entity;

public class Request {
    private String query;
    private String db_name;

    private String filter;

    public Request(String query, String db_name, String filter) {
        this.query = query;
        this.db_name = db_name;
        this.filter = filter;
    }

    public String getQuery() {
        return query;
    }

    public String getDb_name() {
        return db_name;
    }

    public String getFilter() {
        return filter;
    }
}
