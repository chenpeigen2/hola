package cn.ruc.edu.hola.client.entity;

public record Request(String uid, String filter) {
    public Request {
        filter = filter.replaceAll("\\s", "");
    }
}
