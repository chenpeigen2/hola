package cn.ruc.edu.hola.fakeTee.tee.entity;

import java.util.StringJoiner;

public class Request {
    public final String url;

    public final String filter;

    public Request(String url, String filter) {
        this.url = url;
        this.filter = filter;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Request.class.getSimpleName() + "[", "]")
                .add("url='" + url + "'")
                .add("filter='" + filter + "'")
                .toString();
    }
}
