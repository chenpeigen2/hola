package cn.ruc.edu.hola.fakeTee.tee.entity;

public class Request {
    public final String url;

    public final String filter;

    public Request(String url, String filter) {
        this.url = url;
        this.filter = filter;
    }
}
