package cn.ruc.edu.hola.client.entity;

public record ChaincodeRequest(String uid, String filter) {
    public ChaincodeRequest {
        filter = filter.replaceAll("\\s", "");
    }
}
